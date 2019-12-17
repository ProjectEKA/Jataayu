package `in`.org.projecteka.jataayu.provider.model

import com.squareup.moshi.Json

data class Address(@field:Json(name = "use") val use : String,
                   @field:Json(name = "line") val line : String,
                   @field:Json(name = "city") val city : String,
                   @field:Json(name = "district") val district : String,
                   @field:Json(name = "state") val state : String,
                   @field:Json(name = "postalCode") val postalCode : Int)