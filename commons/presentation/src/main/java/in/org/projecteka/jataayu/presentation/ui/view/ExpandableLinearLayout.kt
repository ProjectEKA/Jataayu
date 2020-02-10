package `in`.org.projecteka.jataayu.presentation.ui.view

import `in`.org.projecteka.jataayu.presentation.R
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * This class acts somewhat similar to `SlidingDrawer` in android which has a body and a handle (header in our case).
 * Clicking on this handle/header would hide/show the body.
 *
 * To use this, one has to set a tag "header" to the header part in the xml e.g. android:tag="@string/header"
 * Also, to mention the body, a tag "body" should be added to the body in the xml. e.g. android:tag="@string/body"
 */
class ExpandableLinearLayout : LinearLayout, View.OnClickListener {
    private val ctx: Context
    var isExpanded = true
        private set
    private var header: ViewGroup? = null
    private var body: View? = null
    private var listener: ExpandedStateChangeListener? = null

    constructor(ctx: Context) : super(ctx) {
        this.ctx = ctx
    }

    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        this.ctx = ctx
    }

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        ctx,
        attrs,
        defStyleAttr
    ) {
        this.ctx = ctx
    }

    fun toggleExpandedState() {
        isExpanded = !isExpanded
        onStateChange()
    }

    fun changeState(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        onStateChange()
    }

    private fun onStateChange() {
        toggleExpandableImageStates()
        toggleBodyVisibility()
        if (listener != null) {
            listener!!.onExpandedStateChanged(isExpanded)
        }
    }

    fun onExpandedStateChanged(listener: ExpandedStateChangeListener?) {
        this.listener = listener
    }

    /**
     * Expects a layout with tag - android:tag="@string/header"
     */
    private fun toggleExpandableImageStates() {
        if (header == null) {
            initializeHeaderAndbody()
        }
        for (i in 0 until header!!.childCount) {
            val childView = header!!.getChildAt(i)
            if (childView is StateImageView) {
                childView.refreshState(isExpanded)
            }
        }
    }

    private fun toggleBodyVisibility() {
        val visibility = if (isExpanded) View.VISIBLE else View.GONE
        if (body == null) {
            initializeHeaderAndbody()
        }
        body!!.visibility = visibility
    }

    private fun initializeHeaderAndbody() {
        body = findViewWithTag(ctx.getString(R.string.body))
        header = findViewWithTag<View>(ctx.getString(R.string.header)) as ViewGroup
        header!!.setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onStateChange()
    }

    override fun onClick(v: View) {
        toggleExpandedState()
    }

    interface ExpandedStateChangeListener {
        fun onExpandedStateChanged(expanded: Boolean)
    }
}