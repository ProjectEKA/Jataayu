package `in`.projecteka.jataayu.util.extension

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

fun <T> ViewModel.liveDataOf() = MutableLiveData<T>()