package `in`.org.projecteka.jataayu.core.model

import `in`.org.projecteka.jataayu.core.BR
import `in`.org.projecteka.jataayu.core.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
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