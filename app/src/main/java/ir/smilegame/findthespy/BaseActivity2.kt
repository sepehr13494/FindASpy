package ir.smilegame.findthespy

import android.os.Handler
import ir.smilegame.findthespy.MyApplication.Companion.hasMusic

abstract class BaseActivity2 : BaseActivity(){

    companion object{
        var counter = 0
    }


    private var fromHome = false

    public override fun onStart() {
        super.onStart()
        counter++
    }

    public override fun onStop() {
        super.onStop()
        counter--
        Handler().postDelayed(Runnable {
            if (counter == 0) {
                fromHome = hasMusic
                if (hasMusic){
                    MainActivity.mediaPlayer.pause()
                }
            }
        },1000)
    }
}