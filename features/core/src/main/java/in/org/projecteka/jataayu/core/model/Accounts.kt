package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.model.CareContext
import `in`.org.projecteka.jataayu.core.model.ProviderInfo
import com.google.gson.annotations.SerializedName

data class Accounts(

    @SerializedName("hip") val hip: ProviderInfo,
    @SerializedName("careContexts") val careContexts: List<CareContext>
)