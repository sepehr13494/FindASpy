package ir.boardbazi.findaspy.inGameActivity

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import ir.boardbazi.findaspy.BaseActivity
import ir.boardbazi.findaspy.GameOption
import ir.boardbazi.findaspy.R
import ir.boardbazi.findaspy.Roles
import ir.boardbazi.findaspy.places_activity.Place
import ir.boardbazi.findaspy.places_activity.PlaceAdapter
import kotlinx.android.synthetic.main.activity_in_game.*


class InGameActivity : BaseActivity() {

    private var width:Float = 0f
    private var height:Float = 0f

    lateinit var gameOption:GameOption
    lateinit var roles:Roles
    lateinit var adapter: PlaceAdapter
    lateinit var timer:CountDownTimer
    var mtimeLeft:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_game)

        mGetScreenSize()

        gameOption = Gson().fromJson(intent.getStringExtra("gameOption"), GameOption::class.java)
        roles = Gson().fromJson(intent.getStringExtra("roles"), Roles::class.java)

        countDown((60000*gameOption.time).toLong())

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,6)
        adapter = PlaceAdapter(applicationContext,gameOption.places)
        recyclerView.adapter = adapter

        exit_img.setOnClickListener{
            timer.cancel()
            val dialog = Dialog(this)
            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog .setCancelable(true)
            dialog.setContentView(R.layout.in_game_dialog)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
            dialog.findViewById<TextView>(R.id.d_continue).setOnClickListener{
                countDown(mtimeLeft)
                dialog.dismiss()
            }
            dialog.findViewById<TextView>(R.id.d_result).setOnClickListener{
                dialog.dismiss()
                showResult()
            }
            dialog.findViewById<TextView>(R.id.d_exit).setOnClickListener{
                dialog.dismiss()
                finish()
            }
            dialog.show()
        }

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
                if ((millisUntilFinished/60000) in 1..4){
                    minute_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorOrange))
                    second_tv.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorOrange))
                }
                if ((millisUntilFinished/60000)<1){
                    minute_tv.setTextColor(Color.RED)
                    second_tv.setTextColor(Color.RED)
                }
            }

            override fun onFinish() {
                showResult()
            }

        }.start()
    }

    private fun showResult() {
        timer.cancel()
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
            finish()
        }
        dialog.show()


    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }
}
