package ir.smilegame.findthespy

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import java.util.*

abstract class CafeBazaarActivity : BaseActivity2() {
    lateinit var paymentConnection: Connection
    lateinit var payment: Payment
    var hasSubscribe:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val localSecurityCheck = SecurityCheck.Enable(
            rsaPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDYhxj4XRcjeWDcEYXlXdYPJ/g0rr7lNJWRUEAn6WbjOV7Mm0S7dlc0EeejaA3Wct1B+qeKs/1t5g2kJwgE9yCAgfFrgsVNL8TJQP4NqYRIm1WWyzfdC1Vtb9zE5OdSZJX07MMzPqhUdSS8birEEYWXXeIL57dyuLNQ28R9VqKus/ZWWJ9m7qILbNiJGl6zAi6RIQiKyCfe33+K84hqtLznI5sqP2GKsBrNO1o04xECAwEAAQ=="
        )

        val paymentConfiguration = PaymentConfiguration(
            localSecurityCheck = localSecurityCheck
        )
        Log.d("tag", "onCreate: salam")
        payment = Payment(context = this, config = paymentConfiguration)
        paymentConnection = payment.connect {
            connectionSucceed {
                Log.d("paymentConnection", "onCreate: suceed")
                payment.getSubscribedProducts {
                    querySucceed { purchasedProducts ->
                        Log.d("getSubscribedProducts", "onCreate: " +purchasedProducts.toString())
                        purchasedProducts.forEach{
                            if (it.productId == "3months"){
                                var calendar: Calendar = Calendar.getInstance()
                                calendar.time = Date(it.purchaseTime)
                                calendar.add(Calendar.DATE,90)
                                if (calendar.time.after(Date())){
                                    Log.d("TAG", "onCreate:  oomad inja")
                                    hasSubscribe = true
                                    onSubscribeChange()
                                }
                            }
                        }
                    }
                    queryFailed { throwable ->
                        Log.d("getSubscribedProducts", "onCreate: " +throwable.message)
                    }
                }
            }
            connectionFailed { throwable ->
                Log.d("paymentConnection", "onCreate: failed" + throwable.message)
            }
            disconnected {
                Log.d("paymentConnection", "onCreate: disconnected")
            }
        }
    }

    abstract fun onSubscribeChange()

    override fun onDestroy() {
        super.onDestroy()
        if (paymentConnection != null){
            paymentConnection.disconnect()
        }
    }
}