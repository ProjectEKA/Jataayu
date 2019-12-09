package `in`.org.projecteka.jataayu.ui

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.databinding.ActivityLauncherBinding
import `in`.org.projecteka.jataayu.viewmodel.LauncherViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import org.koin.android.ext.android.inject

class LauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLauncherBinding
    private val viewModel: LauncherViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher)
        binding.lifecycleOwner = this
        observeData()
    }

    private fun observeData() {
        viewModel.sampleModel.observe(this, Observer { sampleModel ->
            sampleModel?.let {
                binding.sampleModel = sampleModel
            }
        })
        viewModel.getLabel()
    }
}
