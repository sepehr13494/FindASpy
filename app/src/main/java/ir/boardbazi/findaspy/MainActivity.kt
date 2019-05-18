package ir.boardbazi.findaspy

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.gson.Gson
import ir.boardbazi.findaspy.places_activity.Place
import ir.boardbazi.findaspy.places_activity.PlacesActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    private var width:Float = 0f
    private var height:Float = 0f

    val persons_array = intArrayOf(R.drawable.a2person,R.drawable.a3person,R.drawable.a4person,
        R.drawable.a5person,R.drawable.a6person,R.drawable.a7person,R.drawable.a8person)

    val spys_array = intArrayOf(R.drawable.spy1,R.drawable.spy2)

    var time = 10
    var persons = 4
    var spys = 1
    var places_count = 12
    var places:ArrayList<Place> = ArrayList()
    var all_places:ArrayList<Place> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGetScreenSize()

        for (s:String in resources.getStringArray(R.array.places)){
            all_places.add(Place(Place.getPlaceCard(s),s))
        }

        person_img.setImageResource(R.drawable.a4person)
        spy_img.setImageResource(R.drawable.spy1)

        player_picker.setOnNumberChangedListener(object : MyNumberPicker.OnNumberChangedListener{
            override fun onNumberChanged(sum: Int) {
                person_img.setImageResource(persons_array[sum-2])
                persons = sum
            }
        })

        spy_picker.setOnNumberChangedListener(object : MyNumberPicker.OnNumberChangedListener{
            override fun onNumberChanged(sum: Int) {
                spy_img.setImageResource(spys_array[sum-1])
                spys = sum
            }
        })

        time_picker.setOnNumberChangedListener(object : MyNumberPicker.OnNumberChangedListener{
            override fun onNumberChanged(sum: Int) {
                time = sum
            }
        })

        place_picker.setOnNumberChangedListener(object : MyNumberPicker.OnNumberChangedListener{
            override fun onNumberChanged(sum: Int) {
                places_count = sum
            }
        })

        info_img.setOnClickListener {
            val dialog = Dialog(this)
            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog .setCancelable(true)
            dialog.setContentView(R.layout.in_game_dialog)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout(height.toInt(),(height*0.85).toInt())

        }


    }

    fun startGame(view: View){
        all_places.shuffle()
        places = ArrayList()
        for(i in 0 until places_count){
            places.add(all_places[i])
        }
        val gameOption  = GameOption(persons,spys,time,places)
        val s = Gson().toJson(gameOption)
        val intent = Intent(this@MainActivity,PlacesActivity::class.java)
        intent.putExtra("gameOption",s)
        startActivity(intent)
    }

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }

}
