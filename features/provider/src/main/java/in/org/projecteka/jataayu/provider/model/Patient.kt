package `in`.org.projecteka.jataayu.provider.model

import com.squareup.moshi.Json

data class Patient(@field:Json(name = "id") val id : String,
                   @field:Json(name = "name") val name : String,
                   @field:Json(name = "gender") val gender : String,
                   @field:Json(name = "contact") val contact : Int,
                   @field:Json(name = "address") val address : Address)