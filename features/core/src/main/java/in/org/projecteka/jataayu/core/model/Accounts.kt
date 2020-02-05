package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class Accounts(

    @SerializedName("hip") val hip: ProviderInfo,
    @SerializedName("careContexts") val careContexts: List<CareContext>
)