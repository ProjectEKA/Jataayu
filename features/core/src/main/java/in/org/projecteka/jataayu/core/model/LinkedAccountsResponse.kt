package `in`.org.projecteka.jataayu.core.model

import com.google.gson.annotations.SerializedName

data class LinkedAccountsResponse (
	@SerializedName("patient") val linkedPatient : LinkedPatient
)