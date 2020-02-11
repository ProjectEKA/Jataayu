package `in`.projecteka.jataayu.ui.testUtil.espresso


import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class CustomRecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPositionOnView(
        position: Int,
        targetViewId: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format(
                            "%s (resource name not found)",
                            Integer.valueOf(recyclerViewId)
                        )
                    }
                }
                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {
                resources = view.resources
                if (childView == null) {
                    val recyclerView: RecyclerView =
                        view.rootView.findViewById<View>(recyclerViewId) as RecyclerView
                    childView =
                        if (recyclerView != null && recyclerView.getId() === recyclerViewId) {
                            recyclerView.getChildAt(position)
                        } else {
                            return false
                        }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView =
                        childView!!.findViewById<View>(targetViewId)
                    view === targetView
                }
            }
        }
    }

}