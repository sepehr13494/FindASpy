package ir.smilegame.findthespy

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.Window
import android.widget.TextView
import ir.smilegame.findthespy.MyApplication.Companion.hasMusic
import ir.smilegame.findthespy.package_activity.PackageActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.setting_img


class MainActivity : BaseActivity2() {

    companion object {
        lateinit var mediaPlayer: MediaPlayer
    }

    private var width:Float = 0f
    private var height:Float = 0f

    val persons_array = intArrayOf(R.drawable.a2person,R.drawable.a3person,R.drawable.a4person,
        R.drawable.a5person,R.drawable.a6person,R.drawable.a7person,R.drawable.a8person)

    val spys_array = intArrayOf(R.drawable.spy1,R.drawable.spy2)

    var time = 10
    var persons = 4
    var spys = 1
    var places_count = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGetScreenSize()

        mediaPlayer = MediaPlayer.create(applicationContext,R.raw.spymusic)
        mediaPlayer.start()

        setting_img.setOnClickListener {
            if (mediaPlayer.isPlaying){
                mediaPlayer.pause()
                setting_img.setImageResource(R.drawable.nosound)
                hasMusic = false
            }else{
                mediaPlayer.start()
                setting_img.setImageResource(R.drawable.sound)
                hasMusic = true
            }
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.start()
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
            dialog.setContentView(R.layout.info)
            dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window.setLayout(height.toInt(),(height*0.85).toInt())
            var text = dialog.findViewById<TextView>(R.id.howToPlaye_tv)
            var link = dialog.findViewById<TextView>(R.id.videoLink).setOnClickListener{
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.aparat.com/v/K5RMg"))
                startActivity(browserIntent)
            }
            var string = text.text
            var spannableString = SpannableString(string)
            var s1 = string.split("\n")
            for (s in s1){
                if (s1.indexOf(s)%2 != 0){
                    spannableString.setSpan(ForegroundColorSpan(Color.DKGRAY),string.indexOf(s),string.indexOf(s)+s.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }else{
                    spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MainActivity,R.color.colorDarkGreen)),string.indexOf(s),string.indexOf(s)+s.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            text.text = spannableString
            dialog.show()

        }

        smile_img.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/com.smile.game"))
            startActivity(browserIntent)
        }


    }

    fun startGame(view: View){

        val intent = Intent(this@MainActivity,PackageActivity::class.java)
        intent.putExtra("persons",persons)
        intent.putExtra("spys",spys)
        intent.putExtra("time",time)
        intent.putExtra("places",places_count)
        startActivity(intent)
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
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        counter = 1
    }

}
