package `in`.org.projecteka.jataayu.provider.model

import com.google.gson.annotations.SerializedName

data class Address(@SerializedName("use") val use : String,
                   @SerializedName("line") val line : String,
                   @SerializedName("city") val city : String,
                   @SerializedName("district") val district : String,
                   @SerializedName("state") val state : String,
                   @SerializedName("postalCode") val postalCode : Int)