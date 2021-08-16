package ir.smilegame.findthespy.package_activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.request.PurchaseRequest
import ir.smilegame.findthespy.*
import ir.smilegame.findthespy.R

import ir.smilegame.findthespy.places_activity.Place
import ir.smilegame.findthespy.places_activity.PlacesActivity
import ir.tapsell.sdk.*
import kotlinx.android.synthetic.main.activity_package.*
import kotlinx.android.synthetic.main.activity_package.recyclerView
import ir.tapsell.sdk.TapsellAdRequestListener
import ir.tapsell.sdk.TapsellAdRequestOptions
import ir.tapsell.sdk.Tapsell
import java.util.*
import kotlin.collections.ArrayList


class PackageActivity : CafeBazaarActivity() {

    private var width:Float = 0f
    private var height:Float = 0f
    private val TAG = "PackageActivity"

    private lateinit var adapter: PackageAdapter

    var places:ArrayList<Place> = ArrayList()
    var main:ArrayList<Place> = ArrayList()

    lateinit var dialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package)

        mGetScreenSize()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 4)
        var names:ArrayList<String> = ArrayList()
        names.add("اصلی")
        names.add("ایران")
        names.add("همه")
        adapter = PackageAdapter(this@PackageActivity, names)
        recyclerView.adapter = adapter

        back.setOnClickListener{ onBackPressed() }
    }

    override fun onSubscribeChange() {}

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }

    override fun onResume() {
        super.onResume()
        if (MyApplication.hasMusic){
            MainActivity.mediaPlayer.start()
        }
    }

    fun startGame(name:Int){
        main = ArrayList()
        if (name == 100){
            for (s:String in resources.getStringArray(R.array.iran)){
                main.add(Place(Place.getPlaceCard(s),s))
            }
            for (s:String in resources.getStringArray(R.array.main)){
                main.add(Place(Place.getPlaceCard(s),s))
            }
        }else{
            for (s:String in resources.getStringArray(name)){
                main.add(Place(Place.getPlaceCard(s),s))
            }
        }
        main.shuffle()
        this.places = ArrayList()
        for(i in 0 until intent.getIntExtra("places",0)){
            this.places.add(main[i])
        }
        val gameOption  = GameOption(intent.getIntExtra("persons",0),intent.getIntExtra("spys",0),intent.getIntExtra("time",0),this.places)
        val s = Gson().toJson(gameOption)
        val intent2 = Intent(this@PackageActivity,PlacesActivity::class.java)
        intent2.putExtra("gameOption",s)
        intent2.putExtra("name",name)
        if (name != R.array.main){
            if(!hasSubscribe){
                dialog = Dialog(this@PackageActivity)
                dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog .setCancelable(false)
                dialog.setContentView(R.layout.update_dialog)
                dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.window.setLayout(height.toInt(),(height*0.65).toInt())
                dialog.findViewById<TextView>(R.id.textView5).text = "برای استفاده از این پکیج لطفا یک تبلیغ ببینید یا از صفحه اصلی اشتراک تهیه کنید"
                dialog.findViewById<TextView>(R.id.later_btn).text = "دیدن تبلیغ"
                dialog.findViewById<TextView>(R.id.later_btn).setOnClickListener{
                    showAd(intent2)
                    Toast.makeText(applicationContext,"در حال آماده سازی",Toast.LENGTH_SHORT).show()
                }
                dialog.findViewById<TextView>(R.id.link_btn).text = "لغو"
                dialog.findViewById<TextView>(R.id.link_btn).setOnClickListener {
                    dialog.cancel()
                }
                dialog.show()
            }else{
                startActivity(intent2)
            }
        }else{
            startActivity(intent2)
        }
    }

    fun showAd(intent: Intent) {
        var rewarded = false
        val options = TapsellAdRequestOptions(TapsellAdRequestOptions.CACHE_TYPE_STREAMED)
        val showOption = TapsellShowOptions()
        val listener = object :TapsellAdShowListener(){
            override fun onError(p0: String?) {
                Log.d(TAG, "onError: Tapsell ::::" + p0)
                Toast.makeText(applicationContext,"متاسفانه قادر به پخش تبلیغ نیستیم",Toast.LENGTH_SHORT).show()
                dialog.cancel()
                super.onError(p0)
            }

            override fun onClosed() {
                super.onClosed()
                if(rewarded) {
                    startActivity(intent)
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
                    dialog.cancel()
                }
            })

    }

    fun openForm(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdyD5cduzaIAC4Y6C4DtcXUmXydW44wRRrkZujdX7yY6Q7njA/viewform?usp=sf_link"))
        startActivity(browserIntent)
    }

}
