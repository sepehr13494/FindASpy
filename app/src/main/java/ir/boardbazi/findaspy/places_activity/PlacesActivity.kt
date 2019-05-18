package ir.boardbazi.findaspy.places_activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Window
import android.widget.TextView
import com.google.gson.Gson
import ir.boardbazi.findaspy.BaseActivity
import ir.boardbazi.findaspy.GameOption
import ir.boardbazi.findaspy.R
import ir.boardbazi.findaspy.game_activity.StartGameActivity

import kotlinx.android.synthetic.main.activity_places.*

class PlacesActivity : BaseActivity() {

    private var width:Float = 0f
    private var height:Float = 0f

    private lateinit var gameOption:GameOption
    private lateinit var adapter: PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        mGetScreenSize()

        gameOption = Gson().fromJson(intent.getStringExtra("gameOption"),GameOption::class.java)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,4)
        adapter = PlaceAdapter(applicationContext,gameOption.places)
        recyclerView.adapter = adapter
        start_btn.setOnClickListener{ startGame() }
        back.setOnClickListener{ onBackPressed() }
        change_btn.setOnClickListener {
            val replacedPlaces = ArrayList<Place>()
            val allPlaces = ArrayList<Place>()
            for (s:String in resources.getStringArray(R.array.places)){
                allPlaces.add(Place(Place.getPlaceCard(s),s))
            }
            val dialog = Dialog(this)
            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog .setCancelable(true)
            dialog.setContentView(R.layout.place_dialog)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout(height.toInt(),(height*0.75).toInt())
            dialog.findViewById<TextView>(R.id.d_place_text).text = "به تعداد ${gameOption.places.size} تا انتخاب کنید."
            val dRecyclerView = dialog.findViewById<RecyclerView>(R.id.dialog_recycler)
            dRecyclerView.setHasFixedSize(true)
            dRecyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = DialogPlaceAdapter(this,allPlaces,gameOption,this.adapter,replacedPlaces,recyclerView,dialog)
            dRecyclerView.adapter = adapter
            dialog.show()

        }

    }

    private fun startGame(){
        val intent = Intent(this@PlacesActivity,StartGameActivity::class.java)
        intent.putExtra("gameOption",Gson().toJson(gameOption))
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
