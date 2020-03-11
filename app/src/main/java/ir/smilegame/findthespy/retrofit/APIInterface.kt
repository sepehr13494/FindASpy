package ir.smilegame.findthespy.retrofit

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface{


    @POST("update.php")
    @FormUrlEncoded
    @Headers("User-Agent:<spy>")
    fun checkUpdate(@Field("version") version: Int): Call<BodyResponse>
}