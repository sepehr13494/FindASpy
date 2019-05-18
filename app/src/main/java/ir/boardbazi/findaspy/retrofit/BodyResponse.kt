package ir.boardbazi.findaspy.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BodyResponse (
    @Expose
    @SerializedName("code")
    val code:Int,

    @Expose
    @SerializedName("link")
    val link:String
)