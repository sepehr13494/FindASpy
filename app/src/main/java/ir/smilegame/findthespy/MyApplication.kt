package ir.smilegame.findthespy

import android.app.Application
import ir.tapsell.sdk.Tapsell

class MyApplication : Application() {
    companion object{
        var hasMusic = true
    }

    override fun onCreate() {
        super.onCreate()
        Tapsell.initialize(this@MyApplication,"lrptnktmdtafnjhrmqrddohmhhqkagmtjpcdtqlbttgjetpeatqbkeabgcsggdhiaiannb")
    }
}