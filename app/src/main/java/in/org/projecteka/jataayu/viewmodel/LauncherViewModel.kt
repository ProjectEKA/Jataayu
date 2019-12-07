package `in`.org.projecteka.jataayu.viewmodel

import `in`.org.projecteka.jataayu.model.SampleModel
import `in`.org.projecteka.jataayu.repository.SampleRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LauncherViewModel(private val sampleRepository: SampleRepository) : ViewModel() {
    var sampleModel = SampleModel(MutableLiveData("Initial"))

    internal fun getLabel() {
        sampleModel.label.postValue(sampleRepository.getSampleText())
    }
}