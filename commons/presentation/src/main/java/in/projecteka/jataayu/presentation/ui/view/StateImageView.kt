package `in`.projecteka.jataayu.presentation.ui.view

import `in`.projecteka.jataayu.presentation.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class StateImageView(context: Context?, attrs: AttributeSet?) :
    AppCompatImageView(context, attrs) {
    private var expanded = false
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (expanded) {
            View.mergeDrawableStates(drawableState, EXPANDED)
        }
        return drawableState
    }

    fun refreshState(expanded: Boolean) {
        this.expanded = expanded
        refreshDrawableState()
    }

    companion object {
        private val EXPANDED = intArrayOf(R.attr.expanded)
    }
}