package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.model.SampleModel
import `in`.org.projecteka.jataayu.viewmodel.LauncherViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {
    private var sampleModel = SampleModel(MutableLiveData("Hellos"))
    private lateinit var binding: ActivityLauncherBinding
    private val viewModel : LauncherViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)
        observeData()
        viewModel.getLabel()
    }

    private fun observeData() {
        viewModel.sampleModel.label.observe(this, Observer { data ->
            binding.sampleModel = sampleModel
            sampleModel.label.postValue(data)
        })
    }
}
