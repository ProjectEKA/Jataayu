package `in`.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class DateOfBirth(
    @SerializedName("date") val date: Int?,
    @SerializedName("month") val month: Int?,
    @SerializedName("year") val year: Int?
)