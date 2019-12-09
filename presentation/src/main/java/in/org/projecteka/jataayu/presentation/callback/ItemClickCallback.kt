package `in`.org.projecteka.jataayu.presentation.callback

import androidx.databinding.ViewDataBinding

interface ItemClickCallback {
    fun performItemClickAction(i: IDataBinding, binding: ViewDataBinding)
}
