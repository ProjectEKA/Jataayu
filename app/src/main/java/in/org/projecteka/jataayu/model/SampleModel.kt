package `in`.org.projecteka.jataayu.model

import androidx.lifecycle.MutableLiveData
import java.io.Serializable

data class SampleModel(val label: MutableLiveData<String>) : Serializable
