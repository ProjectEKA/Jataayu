package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class RecoverCmidResponse(
    @SerializedName("cmId") val cmId: String
)