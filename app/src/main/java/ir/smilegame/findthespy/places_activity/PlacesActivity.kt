package ir.smilegame.findthespy.places_activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.google.gson.Gson
import ir.smilegame.findthespy.BaseActivity2
import ir.smilegame.findthespy.GameOption
import ir.smilegame.findthespy.MainActivity
import ir.smilegame.findthespy.MyApplication.Companion.hasMusic
import ir.smilegame.findthespy.R
import ir.smilegame.findthespy.game_activity.StartGameActivity

import kotlinx.android.synthetic.main.activity_places.*

open class PlacesActivity : BaseActivity2() {

    private var width:Float = 0f
    private var height:Float = 0f

    private lateinit var gameOption:GameOption
    private lateinit var adapter:PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)

        mGetScreenSize()

        gameOption = Gson().fromJson(intent.getStringExtra("gameOption"),GameOption::class.java)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(applicationContext, 4)
        adapter = PlaceAdapter(applicationContext,gameOption.places)
        recyclerView.adapter = adapter
        start_btn.setOnClickListener{ startGame() }
        change_btn.setOnClickListener {
            val replacedPlaces = ArrayList<Place>()
            val allPlaces = ArrayList<Place>()
            if (intent.getIntExtra("name",0) == 100){
                for (s:String in resources.getStringArray(R.array.main)){
                    allPlaces.add(Place(Place.getPlaceCard(s),s))
                }
                for (s:String in resources.getStringArray(R.array.iran)){
                    allPlaces.add(Place(Place.getPlaceCard(s),s))
                }
            }else{
                for (s:String in resources.getStringArray(intent.getIntExtra("name",0))){
                    allPlaces.add(Place(Place.getPlaceCard(s),s))
                }
            }
            val dialog = Dialog(this)
            dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog .setCancelable(true)
            dialog.setContentView(R.layout.place_dialog)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout((height*1.5).toInt(),(height*0.90).toInt())
            val dRecyclerView = dialog.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.dialog_recycler)
            val savetv = dialog.findViewById<TextView>(R.id.save_tv)
            dRecyclerView.setHasFixedSize(true)
            dRecyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
            val adapter = DialogPlaceAdapter(this,allPlaces,gameOption,this.adapter,replacedPlaces,recyclerView,dialog,savetv)
            dRecyclerView.adapter = adapter
            dialog.show()

        }
        back.setOnClickListener{ onBackPressed() }

    }


    private fun startGame(){
        val intent = Intent(this@PlacesActivity,StartGameActivity::class.java)
        intent.putExtra("gameOption",Gson().toJson(gameOption))
        startActivity(intent)
        finish()
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
        if (hasMusic){
            MainActivity.mediaPlayer.start()
        }
    }

}
