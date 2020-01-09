package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.consent.R
import `in`.org.projecteka.jataayu.presentation.ui.fragment.BaseFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class ConsentListFragment : BaseFragment() {
    companion object {
        fun newInstance() = ConsentListFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consent_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}


