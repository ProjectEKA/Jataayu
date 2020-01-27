package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class LinkedPatient(
    @SerializedName("id") val id: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("links") val links: List<Links?>
) {
    fun getFullName(): String {
        return "$firstName $lastName"
    }
}