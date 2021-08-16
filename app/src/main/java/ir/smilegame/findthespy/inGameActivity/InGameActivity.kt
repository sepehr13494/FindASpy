package ir.smilegame.findthespy.inGameActivity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import ir.smilegame.findthespy.*
import ir.smilegame.findthespy.MyApplication.Companion.hasMusic
import ir.smilegame.findthespy.R
import ir.smilegame.findthespy.package_activity.PackageAdapter
import ir.smilegame.findthespy.places_activity.Place
import ir.smilegame.findthespy.places_activity.PlaceAdapter
import ir.tapsell.sdk.*
import kotlinx.android.synthetic.main.activity_in_game.*
import kotlinx.android.synthetic.main.activity_in_game.exit_img
import kotlinx.android.synthetic.main.activity_in_game.setting_img
import java.util.logging.Logger
import kotlin.random.Random


class InGameActivity : CafeBazaarActivity() {

    private var width:Float = 0f
    private var height:Float = 0f

    lateinit var gameOption:GameOption
    lateinit var roles:Roles
    lateinit var adapter: PlaceAdapter
    lateinit var timer:CountDownTimer
    var mtimeLeft:Long = 0
    lateinit var mediaPlayer:MediaPlayer
    lateinit var ringPlayer:MediaPlayer
    lateinit var startPlayer:MediaPlayer
    var showAgain:Boolean = true
    var prefrence: SharedPreferences? = null
    val PREF_NAME = "gamecount"
    var outPlayers:ArrayList<Int> = ArrayList()
    var gameFinished = false

    lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_game)

        prefrence = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        mediaPlayer = MediaPlayer.create(applicationContext,R.raw.gameending)
        ringPlayer = MediaPlayer.create(applicationContext,R.raw.ring)
        startPlayer = MediaPlayer.create(applicationContext,R.raw.startgame)

        mGetScreenSize()

        setting_img.setOnClickListener {
            if (MainActivity.mediaPlayer.isPlaying){
                MainActivity.mediaPlayer.pause()
                setting_img.setImageResource(R.drawable.nosound)
                hasMusic = false
            }else{
                MainActivity.mediaPlayer.start()
                setting_img.setImageResource(R.drawable.sound)
                hasMusic = true
            }
        }

        gameOption = Gson().fromJson(intent.getStringExtra("gameOption"), GameOption::class.java)
        roles = Gson().fromJson(intent.getStringExtra("roles"), Roles::class.java)

        Logger.getLogger("role").warning(Gson().toJson(roles))
        Logger.getLogger("object").warning(Gson().toJson(gameOption))

        mtimeLeft = (60000*gameOption.time).toLong()

        countDown((60000*gameOption.time).toLong())

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 6)
        adapter = PlaceAdapter(applicationContext,gameOption.places)
        recyclerView.adapter = adapter

        exit_img.setOnClickListener{
            showExitDialog(gameFinished)
        }
        optiontv.setOnClickListener{
            pause()
            goForOption()
        }

    }

    override fun onSubscribeChange() {}

    fun goForOption(){
        dialog = Dialog(this@InGameActivity)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog.setContentView(R.layout.update_dialog)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window.setLayout(height.toInt(),(height*0.65).toInt())

        if (hasSubscribe){
            dialog.findViewById<TextView>(R.id.textView5).text = "میتونید از یک شهروند رو نمایی کنید ولی به همان نسبت از کارت ها حذف میشود"
            dialog.findViewById<TextView>(R.id.later_btn).text = "باشه انجام بشه"
            dialog.findViewById<TextView>(R.id.later_btn).setOnClickListener{
                if (((gameOption.places.size)/(gameOption.persons-outPlayers.size))>=gameOption.places.size || (gameOption.persons-outPlayers.size)==1){
                    Toast.makeText(applicationContext,"دیگه جا نداره", Toast.LENGTH_SHORT).show()
                }else{
                    showOnePerson()
                }

            }
        }else{
            dialog.findViewById<TextView>(R.id.textView5).text = "میتونید با دیدن یک تبلیغ از یک شهروند رو نمایی کنید ولی به همان نسبت از کارت ها حذف میشود"
            dialog.findViewById<TextView>(R.id.later_btn).text = "دیدن تبلیغ"
            dialog.findViewById<TextView>(R.id.later_btn).setOnClickListener{
                if (((gameOption.places.size)/(gameOption.persons-outPlayers.size))>=gameOption.places.size || (gameOption.persons-outPlayers.size)==1){
                    Toast.makeText(applicationContext,"دیگه جا نداره", Toast.LENGTH_SHORT).show()
                }else{
                    showAd()
                    Toast.makeText(applicationContext,"در حال آماده سازی", Toast.LENGTH_SHORT).show()
                }

            }
        }
        dialog.findViewById<TextView>(R.id.link_btn).text = "لغو"
        dialog.findViewById<TextView>(R.id.link_btn).setOnClickListener {
            dialog.cancel()
            resume()
        }
        dialog.show()
    }

    private fun showExitDialog(isGameEnded:Boolean) {
        pause()
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.in_game_dialog)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
        dialog.findViewById<TextView>(R.id.d_continue).setOnClickListener{
            dialog.dismiss()
            resume()
        }
        dialog.findViewById<TextView>(R.id.d_result).setOnClickListener{
            dialog.dismiss()
            showResult()
        }
        dialog.findViewById<TextView>(R.id.d_exit).setOnClickListener{
            dialog.dismiss()
            onfinish()
        }
        if (isGameEnded){
            dialog.findViewById<TextView>(R.id.d_continue).text = "کمک"
            dialog.findViewById<TextView>(R.id.d_continue).setOnClickListener{
                goForOption()
            }
        }
        dialog.show()
    }

    fun showAd() {
        var rewarded = false
        val options = TapsellAdRequestOptions(TapsellAdRequestOptions.CACHE_TYPE_STREAMED)
        val showOption = TapsellShowOptions()
        val listener = object : TapsellAdShowListener(){
            override fun onError(p0: String?) {
                Toast.makeText(applicationContext,"متاسفانه قادر به پخش تبلیغ نیستیم",Toast.LENGTH_SHORT).show()
                dialog.cancel()
                resume()
                super.onError(p0)
            }

            override fun onClosed() {
                super.onClosed()
                if(rewarded) {
                    showOnePerson()
                }else{
                    resume()
                }
                dialog.cancel()

            }

            override fun onRewarded(p0: Boolean) {
                super.onRewarded(p0)
                if(p0){
                    rewarded = true
                }
            }
        }
        showOption.rotationMode = TapsellShowOptions.ROTATION_LOCKED_LANDSCAPE
        showOption.isBackDisabled = false
        showOption.isShowDialog = true
        Tapsell.requestAd(applicationContext,
            "5e66c4961492be00011aad04",
            options,
            object : TapsellAdRequestListener() {
                override fun onAdAvailable(adId: String?) {
                    Tapsell.showAd(
                        applicationContext,
                        "5e66c4961492be00011aad04",
                        adId,
                        showOption,
                        listener
                    )
                }

                override fun onError(message: String?) {
                    Toast.makeText(applicationContext,"متاسفانه قادر به پخش تبلیغ نیستیم",Toast.LENGTH_SHORT).show()
                    resume()
                    dialog.cancel()
                }
            })

    }

    private fun showOnePerson() {
        dialog.cancel()
        gameOption.places.shuffle()
        for(place:Place in gameOption.places){
            if(place.name == roles.place.name){
                gameOption.places.remove(place)
                break
            }
        }
        for(i in 0 until ((gameOption.places.size+1)/(gameOption.persons-outPlayers.size))){
            Logger.getLogger("kooooze").warning(i.toString())
            gameOption.places.removeAt(i)
        }
        gameOption.places.add(roles.place)
        gameOption.places.shuffle()
        adapter = PlaceAdapter(this@InGameActivity,gameOption.places)
        recyclerView.adapter = adapter
        var find = false
        var k:Int
        do {
            k = Random.nextInt(1,roles.players.size)
            Logger.getLogger("koooo").warning(k.toString())
            if(roles.players[k-1] != "جاسوس" && !outPlayers.contains(k)){
                find = true
                outPlayers.add(k)
            }
        }while (!find)
        val dialog = Dialog(this@InGameActivity)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog.setContentView(R.layout.new_option)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
        dialog.findViewById<TextView>(R.id.newOptiontv).text = "بازیکن $k جاسوس نیست"
        dialog.findViewById<TextView>(R.id.newOptionBtn).setOnClickListener{
            dialog.dismiss()
            resume()
        }
        dialog.show()
    }

    private fun onfinish() {
        var i = prefrence?.getInt("count",0)
        i = i?.plus(1)
        /*if (i != null) {
            if (i==2){
                dialog = Dialog(this@InGameActivity)
                dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog .setCancelable(false)
                dialog.setContentView(R.layout.update_dialog)
                dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.window.setLayout(height.toInt(),(height*0.65).toInt())
                dialog.findViewById<TextView>(R.id.textView5).text = "به ما امتیاز میدید؟"
                dialog.findViewById<TextView>(R.id.later_btn).text = "میدم"
                dialog.findViewById<TextView>(R.id.later_btn).setOnClickListener{
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/ir.smilegame.findthespy"))
                    startActivity(browserIntent)
                }
                dialog.findViewById<TextView>(R.id.link_btn).text = "لغو"
                dialog.findViewById<TextView>(R.id.link_btn).setOnClickListener {
                    dialog.cancel()
                }
                dialog.show()
            }
            var editor = prefrence?.edit()
            editor?.putInt("count",i)
            editor?.apply()
        }*/
        this@InGameActivity.finish()
    }

    private fun countDown(timeLeft: Long) {
        timer = object : CountDownTimer(timeLeft,1000.toLong()){
            override fun onTick(millisUntilFinished: Long) {
                mtimeLeft = millisUntilFinished
                var second_remaining = millisUntilFinished/1000
                minute_tv.text = (second_remaining/60).toString()
                second_tv.text = ":"
                second_tv.append(second_remaining.minus((second_remaining/60)*60).toString())
                if (second_remaining.minus((second_remaining/60)*60) in 0..9){
                    second_tv.text = ":"
                    second_tv.append("0")
                    second_tv.append(second_remaining.minus((second_remaining/60)*60).toString())
                }
                if ((millisUntilFinished/6000) == 39.toLong()){
                    minute_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorOrange))
                    second_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorOrange))
                }
                if ((millisUntilFinished/6000) == 14.toLong()){
                    if (hasMusic){
                        MainActivity.mediaPlayer.pause()
                        mediaPlayer.start()
                        setting_img.setOnClickListener {
                            if (mediaPlayer.isPlaying){
                                mediaPlayer.pause()
                                setting_img.setImageResource(R.drawable.nosound)
                                hasMusic = false
                            }else{
                                mediaPlayer.start()
                                mediaPlayer.setOnCompletionListener {
                                    mediaPlayer.stop()
                                }
                                setting_img.setImageResource(R.drawable.sound)
                                hasMusic = true
                            }
                        }
                    }
                    minute_tv.setTextColor(Color.RED)
                    second_tv.setTextColor(Color.RED)
                }

            }

            override fun onFinish() {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                }
                if (!gameFinished)
                showExitDialog(true)
                if (hasMusic && !gameFinished){
                    ringPlayer.start()
                    ringPlayer.setOnCompletionListener {
                        ringPlayer.release()
                    }
                    gameFinished = true
                }
            }

        }.start()
    }

    private fun showResult() {
        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog.setContentView(R.layout.result)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
        dialog.findViewById<ImageView>(R.id.d_place_icon).setImageResource(Place.getPlaceCard(roles.place.name))
        dialog.findViewById<TextView>(R.id.d_place_name).text = roles.place.name
        var set_first = false
        for (i in 0 until roles.players.size){
            if(roles.players[i]=="جاسوس")
            if (!set_first){
                dialog.findViewById<TextView>(R.id.d_first_spy_tv).text = "بازیکن " + (i+1)
                set_first = true
            }else{
                dialog.findViewById<TextView>(R.id.d_second_spy_tv).text = "بازیکن " + (i+1)
                dialog.findViewById<TextView>(R.id.d_second_spy_tv).visibility = View.VISIBLE

            }
        }
        dialog.findViewById<TextView>(R.id.d_ok_btn).setOnClickListener{
            dialog.dismiss()
            onfinish()
        }
        dialog.show()


    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        mediaPlayer.release()
    }

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }


    override fun onBackPressed() {
        showExitDialog(gameFinished)
    }

    override fun onResume() {
        super.onResume()
        resume()

    }

    private fun resume() {
        if (mtimeLeft<(60000*gameOption.time)-2000.toLong()){
            timer.cancel()
            countDown(mtimeLeft)

        }
        if (!hasMusic){
            setting_img.setImageResource(R.drawable.nosound)
        }else{
            if (mtimeLeft>(60000*gameOption.time)-2000.toLong()){
                startPlayer.start()
            }
            if ((mtimeLeft/6000) in 1..14){
                mediaPlayer.start()
            }else{
                MainActivity.mediaPlayer.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    private fun pause() {
        timer.cancel()
        if (MainActivity.mediaPlayer.isPlaying){
            MainActivity.mediaPlayer.pause()
        }
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }
}
