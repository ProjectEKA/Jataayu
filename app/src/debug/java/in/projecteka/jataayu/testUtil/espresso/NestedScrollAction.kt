import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher

class NestedScrollAction : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return allOf(
                withEffectiveVisibility(Visibility.VISIBLE), isDescendantOfA(
                    anyOf(
                        isAssignableFrom(ScrollView::class.java),
                        isAssignableFrom(HorizontalScrollView::class.java),
                        isAssignableFrom(NestedScrollView::class.java)
                    )
                )
            )
        }

        override fun getDescription(): String? {
            return null
        }

        override fun perform(uiController: UiController, view: View) {
            ScrollToAction().perform(uiController, view)
        }
    }