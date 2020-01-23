package `in`.org.projecteka.jataayu.consent.model

import `in`.org.projecteka.jataayu.core.model.CareContext
import `in`.org.projecteka.jataayu.core.model.Hip
import com.google.gson.annotations.SerializedName

data class Links (
	@SerializedName("hip") val hip : Hip,
	@SerializedName("careContexts") val careContexts : List<CareContext>
)