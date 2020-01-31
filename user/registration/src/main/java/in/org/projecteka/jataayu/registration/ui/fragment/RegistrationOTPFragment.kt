package `in`.org.projecteka.jataayu.registration.ui.fragment


import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import `in`.org.projecteka.jataayu.registration.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RegistrationOTPFragment : BaseFragment() {

    companion object{
        fun newInstance() = RegistrationOTPFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ver, container, false)
    }


}
