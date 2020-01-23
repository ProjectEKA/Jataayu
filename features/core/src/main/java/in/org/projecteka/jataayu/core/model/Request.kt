package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("hip") val hip: Hip
)