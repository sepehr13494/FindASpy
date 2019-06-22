package ir.boardbazi.findaspy.game_activity


import android.os.Bundle

import kotlinx.android.synthetic.main.activity_start_game.*
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.TextView
import com.google.gson.Gson
import ir.boardbazi.findaspy.*
import ir.boardbazi.findaspy.MyApplication.Companion.hasMusic
import ir.boardbazi.findaspy.inGameActivity.InGameActivity
import ir.boardbazi.findaspy.places_activity.Place
import kotlin.random.Random


class StartGameActivity : BaseActivity2() {

    private var width:Float = 0f
    private var height:Float = 0f

    private lateinit var gameOption:GameOption
    lateinit var place: Place
    var players = ArrayList<String>()
    var i:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

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

        group.visibility = View.GONE

        gameOption = Gson().fromJson(intent.getStringExtra("gameOption"), GameOption::class.java)

        place = gameOption.places[Random.nextInt(gameOption.places.size-1)]

        for (i in 0 until gameOption.persons){
            players.add("شهروند")
        }
        for (i in 0 until gameOption.spys){
            players.add("جاسوس")
        }
        players.shuffle()

        player_name_tv.text = "بازیکن "
        player_name_tv.append((i+1).toString())
        secondSpy_tv.text = ""
        card_img.setBackgroundResource(R.drawable.card_bg)
        group2.visibility = View.GONE
        if (players[i] == "شهروند"){
            setCard(Place.getPlaceCard(place.name),place.name)
            player_role_tv.text = "شهروند"
            player_role_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorGreen))
            player_task_tv.text = "جاسوس رو پیدا کن"
        }else{
            setCard(R.drawable.spy, "جاسوس")
            player_role_tv.text = "جاسوس"
            player_role_tv.setTextColor(Color.RED)
            player_task_tv.text = "مکان رو پیدا کن"
            for (j in 0 until players.size){
                if (players[j] == "جاسوس" && i!=j){
                    secondSpy_tv.text = "جاسوس دوم بازیکن"
                    secondSpy_tv.append((j+1).toString())
                    secondSpy_tv.setTextColor(Color.RED)
                }
            }
        }

        confirm_btn.setOnClickListener{
            card_img.isEnabled = true
            i++
            if (i<players.size){
                secondSpy_tv.text = ""
                player_name_tv.text = "بازیکن "
                player_name_tv.append((i+1).toString())
                newCard()
                if (players[i] == "شهروند"){
                    setCard(Place.getPlaceCard(place.name), place.name)
                    player_role_tv.text = "شهروند"
                    player_role_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorGreen))
                    player_task_tv.text = "جاسوس رو پیدا کن"
                }else{
                    setCard(R.drawable.spy, "جاسوس")
                    player_role_tv.text = "جاسوس"
                    player_role_tv.setTextColor(Color.RED)
                    player_task_tv.text = "مکان رو پیدا کن"
                    for (j in 0 until players.size){
                        if (players[j] == "جاسوس" && i!=j){
                            secondSpy_tv.visibility = View.VISIBLE
                            secondSpy_tv.text = "جاسوس دوم بازیکن"
                            secondSpy_tv.append((j+1).toString())
                            secondSpy_tv.setTextColor(Color.RED)
                        }
                    }
                }
            }else{
                card_img.isEnabled = false
                newCard()
                Handler().postDelayed({
                    val s = Gson().toJson(gameOption)
                    val roles = Gson().toJson(Roles(place,players))
                    var intent = Intent(this@StartGameActivity, InGameActivity::class.java)
                    intent.putExtra("gameOption",s)
                    intent.putExtra("roles",roles)
                    startActivity(intent)
                    finish()
                },2000)
            }
        }

        exit_img.setOnClickListener{
            val dialog = Dialog(this)
            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog .setCancelable(false)
            dialog.setContentView(R.layout.in_game_dialog)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
            dialog.findViewById<TextView>(R.id.d_continue).setOnClickListener{
                dialog.dismiss()
            }
            dialog.findViewById<TextView>(R.id.d_result).visibility = View.GONE
            dialog.findViewById<TextView>(R.id.d_exit).setOnClickListener{
                dialog.dismiss()
                finish()
            }
            dialog.show()
        }

    }

    private fun setCard(drawable: Int, name: String) {
        card_img.setOnClickListener {
            card_img.isEnabled = false
            val oa1 = ObjectAnimator.ofFloat(card_img, "scaleX", 1f, 0f)
            val oa2 = ObjectAnimator.ofFloat(card_img, "scaleX", 0f, 1f)
            oa1.interpolator = DecelerateInterpolator()
            oa2.interpolator = AccelerateDecelerateInterpolator()
            place_icon.setImageResource(drawable)
            place_name.text = name
            oa1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    card_img.setBackgroundResource(R.drawable.card_frame)
                    group2.visibility = View.VISIBLE
                    oa2.start()
                }
            })
            oa1.start()

            val objectAnimator = ObjectAnimator.ofFloat(card_img, "translationX", 0f, (-width/3))
            objectAnimator.duration = 1000
            objectAnimator.start()
            group.visibility = View.VISIBLE
        }
    }

    private fun newCard(){
        group.visibility = View.GONE
        group2.visibility = View.GONE
        val oa1 = ObjectAnimator.ofFloat(card_img, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(card_img, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                card_img.setBackgroundResource(R.drawable.card_bg)
                oa2.start()
            }
        })
        oa1.start()

        val objectAnimator = ObjectAnimator.ofFloat(card_img, "translationX", (-width/3), 0f)
        objectAnimator.duration = 1000
        objectAnimator.start()
    }

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }

    override fun onResume() {
        super.onResume()
        if (!hasMusic){
            setting_img.setImageResource(R.drawable.nosound)
        }else{
            MainActivity.mediaPlayer.start()
        }
    }
}
