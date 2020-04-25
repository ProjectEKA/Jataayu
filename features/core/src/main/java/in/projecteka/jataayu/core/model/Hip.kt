package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import com.google.gson.annotations.SerializedName

interface HipHiuIdentifiable {
	fun getId(): String
}

data class Hip (
	@SerializedName("id") private val id : String,
	@SerializedName("name") var name : String
) : IDataBindingModel, HipHiuIdentifiable {
	override fun layoutResId(): Int {
		return R.layout.hip_item
	}

	override fun dataBindingVariable(): Int {
		return BR.hip
	}

	override fun getId(): String {
		return id
	}
}