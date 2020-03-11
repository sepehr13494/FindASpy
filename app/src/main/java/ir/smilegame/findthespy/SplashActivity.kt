package ir.smilegame.findthespy

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.TextView
import ir.smilegame.findthespy.retrofit.APIClient
import ir.smilegame.findthespy.retrofit.BodyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger


class SplashActivity : BaseActivity() {

    private var width:Float = 0f
    private var height:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mGetScreenSize()
        getUpdate()


    }

    private fun getUpdate() {
        val call: Call<BodyResponse> = APIClient.getClient.checkUpdate(BuildConfig.VERSION_CODE)
        call.enqueue(object : Callback<BodyResponse>{
            override fun onResponse(call: Call<BodyResponse>, response: Response<BodyResponse>) {
                when {
                    response.body()!!.code == 200 -> {
                        goToGame()
                    }
                    response.body()!!.code == 300 -> {
                        val dialog = Dialog(this@SplashActivity)
                        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog .setCancelable(false)
                        dialog.setContentView(R.layout.update_dialog)
                        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.window.setLayout(height.toInt(),(height*0.65).toInt())
                        dialog.findViewById<TextView>(R.id.later_btn).setOnClickListener{
                            goToGame()
                        }
                        dialog.findViewById<TextView>(R.id.link_btn).setOnClickListener {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.link))
                            startActivity(browserIntent)
                            finish()
                        }
                        dialog.show()
                    }
                    response.body()!!.code == 500 -> {
                        val dialog = Dialog(this@SplashActivity)
                        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog .setCancelable(false)
                        dialog.setContentView(R.layout.update_dialog)
                        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.window.setLayout(height.toInt(),(height*0.65).toInt())
                        dialog.findViewById<TextView>(R.id.later_btn).visibility = View.GONE
                        dialog.findViewById<TextView>(R.id.link_btn).setOnClickListener {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(response.body()!!.link))
                            startActivity(browserIntent)
                            finish()
                        }
                        dialog.show()
                    }
                }
            }

            override fun onFailure(call: Call<BodyResponse>, t: Throwable) {
                Logger.getLogger(SplashActivity::class.java.name).warning(t.message)
                goToGame()
            }

        })
    }

    fun goToGame(){
        Handler().postDelayed(Runnable {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },1500)
    }

    private fun mGetScreenSize() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = size.x.toFloat()
        height = size.y.toFloat()
    }



}
