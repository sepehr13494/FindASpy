package ir.boardbazi.findaspy.inGameActivity

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import ir.boardbazi.findaspy.*
import ir.boardbazi.findaspy.MyApplication.Companion.hasMusic
import ir.boardbazi.findaspy.R
import ir.boardbazi.findaspy.places_activity.Place
import ir.boardbazi.findaspy.places_activity.PlaceAdapter
import ir.tapsell.sdk.*
import kotlinx.android.synthetic.main.activity_in_game.*
import kotlinx.android.synthetic.main.activity_in_game.exit_img
import kotlinx.android.synthetic.main.activity_in_game.setting_img
import android.content.Intent
import android.net.Uri


class InGameActivity : BaseActivity2() {

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

        mtimeLeft = (60000*gameOption.time).toLong()

        countDown((60000*gameOption.time).toLong())

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,6)
        adapter = PlaceAdapter(applicationContext,gameOption.places)
        recyclerView.adapter = adapter

        exit_img.setOnClickListener{
            showExitDialog(false)
        }

    }

    private fun showExitDialog(isGameEnded:Boolean) {
        pause()
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
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
            dialog.findViewById<TextView>(R.id.d_continue).visibility = View.GONE
        }
        dialog.show()
    }

    private fun onfinish() {
        var i = prefrence?.getInt("count",0)
        i = i?.plus(1)
        if (i != null) {
            if (i==2){
                val intent = Intent(Intent.ACTION_EDIT)
                intent.data = Uri.parse("bazaar://details?id=" + "ir.boardbazi.findaspy")
                intent.setPackage("com.farsitel.bazaar")
                startActivity(intent)
            }
            var editor = prefrence?.edit()
            editor?.putInt("count",i)
            editor?.apply()
        }
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
                if((mtimeLeft/6000) == 160.toLong()){
                    if (showAgain){
                        Toast.makeText(this@InGameActivity,"با کلیک کردن رو دکمه بازگشت گوشی میتوانید از تبلیغ خارج شوید",Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            pause()
                            showAd()
                        },2000)
                    }
                    showAgain = false
                }
                if((mtimeLeft/6000) == 120.toLong()){
                    if (showAgain){
                        Toast.makeText(this@InGameActivity,"با کلیک کردن رو دکمه بازگشت گوشی میتوانید از تبلیغ خارج شوید",Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            pause()
                            showAd()
                        },2000)
                    }
                    showAgain = false
                }

                if((mtimeLeft/6000) == 70.toLong()){
                    if (showAgain){
                        Toast.makeText(this@InGameActivity,"با کلیک کردن رو دکمه بازگشت گوشی میتوانید از تبلیغ خارج شوید",Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            pause()
                            showAd()
                        },2000)
                    }
                    showAgain = false
                }
                if (gameOption.time>6){
                    if((mtimeLeft/6000) == 40.toLong()){
                        if (showAgain){
                            Toast.makeText(this@InGameActivity,"با کلیک کردن رو دکمه بازگشت گوشی میتوانید از تبلیغ خارج شوید",Toast.LENGTH_SHORT).show()
                            Handler().postDelayed(Runnable {
                                pause()
                                showAd()
                            },2000)
                        }
                        showAgain = false
                    }
                }
            }

            override fun onFinish() {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                }
                if (hasMusic){
                    ringPlayer.start()
                    ringPlayer.setOnCompletionListener {
                        ringPlayer.release()
                    }
                }
                showExitDialog(true)
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

    fun showAd() {
        var options = TapsellAdRequestOptions(TapsellAdRequestOptions.CACHE_TYPE_STREAMED)
        var showOption = TapsellShowOptions()
        showOption.rotationMode = TapsellShowOptions.ROTATION_LOCKED_LANDSCAPE
        showOption.isBackDisabled = false
        Tapsell.requestAd(this, null, options,object : TapsellAdRequestListener{
            override fun onAdAvailable(ad: TapsellAd?) {

                if (ad != null) {
                    ad.show(this@InGameActivity, showOption, object : TapsellAdShowListener{
                        override fun onOpened(p0: TapsellAd?) {

                        }

                        override fun onClosed(p0: TapsellAd?) {
                            resume()
                            Handler().postDelayed(Runnable {
                                showAgain = true
                            },10000)
                        }

                    })
                }
            }

            override fun onExpiring(p0: TapsellAd?) {
                Toast.makeText(this@InGameActivity,"تبلیغ منقضی شده...",Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable {
                    resume()
                    Handler().postDelayed(Runnable {
                        showAgain = true
                    },10000)
                },2500)
            }

            override fun onNoAdAvailable() {
                Toast.makeText(this@InGameActivity,"تبلیغ نداریم ...",Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable {
                    resume()
                    Handler().postDelayed(Runnable {
                        showAgain = true
                    },10000)
                },2500)
            }

            override fun onError(p0: String?) {
                Toast.makeText(this@InGameActivity,p0,Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable {
                    resume()
                    Handler().postDelayed(Runnable {
                        showAgain = true
                    },10000)
                },2500)
            }

            override fun onNoNetwork() {
                Toast.makeText(this@InGameActivity,"اتصال به سرور نداریم ...",Toast.LENGTH_SHORT).show()
                Handler().postDelayed(Runnable {
                    resume()
                    Handler().postDelayed(Runnable {
                        showAgain = true
                    },10000)
                },2500)

            }

        })
    }

    override fun onBackPressed() {
        showExitDialog(false)
    }

    override fun onResume() {
        super.onResume()
        resume()

    }

    private fun resume() {
        if (mtimeLeft<(60000*gameOption.time)-2000.toLong()){
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
