package `in`.projecteka.jataayu.core.model

import `in`.projecteka.jataayu.core.BR
import `in`.projecteka.jataayu.core.R
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import com.google.gson.annotations.SerializedName

data class Hip (
	@SerializedName("id") val id : String,
	@SerializedName("name") val name : String
) : IDataBindingModel {
	override fun layoutResId(): Int {
		return R.layout.hip_item
	}

	override fun dataBindingVariable(): Int {
		return BR.hip
	}
}