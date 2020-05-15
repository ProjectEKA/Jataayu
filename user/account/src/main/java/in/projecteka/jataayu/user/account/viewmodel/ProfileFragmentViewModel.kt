package `in`.projecteka.jataayu.user.account.viewmodel

import `in`.projecteka.jataayu.presentation.BaseViewModel
import `in`.projecteka.jataayu.util.livedata.SingleLiveEvent
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import java.util.*

class ProfileFragmentViewModel(val preferenceRepository: PreferenceRepository): BaseViewModel() {

    private var yob: Int? = null
    var isEditMode = SingleLiveEvent<Boolean>()

    companion object {
        private const val INDIA_COUNTRY_CODE = "+91"
        private const val COUNTRY_CODE_SEPARATOR = "-"
        private const val YOB = "yyyy"
    }

    fun yearOfBirth(): String{
        val yob = preferenceRepository.yearOfBirth
        if (yob == 0) return ""
        else return yob.toString()
    }

    fun init() {
        isEditMode.value = false
    }

    internal fun getYearsToPopulate(): List<String> {
        val years = arrayListOf<String>(YOB)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (year in currentYear downTo (currentYear - 120)) {
            years.add(year.toString())
        }
        return years
    }

    fun selectedYoB(yob: Int){
        this.yob = yob
    }
}