package `in`.org.projecteka.jataayu.testUtil.espresso

import android.view.View
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CustomMatcher
import org.hamcrest.Matcher

class WaitForViewMatching(private val viewMatcher: Matcher<View?>, private val waitTimeout: Int) :
    ViewAction {
    override fun getConstraints(): CustomMatcher<View> {
        return anyView()!!
    }

    override fun getDescription(): String {
        return "wait for $viewMatcher"
    }

    override fun perform(
        uiController: UiController,
        view: View
    ) {
        val matcherIdlingResource = MatcherIdlingResource(waitTimeout, viewMatcher, view)
        IdlingRegistry.getInstance().register(matcherIdlingResource)
        uiController.loopMainThreadUntilIdle()
        IdlingRegistry.getInstance().unregister(matcherIdlingResource)
        if (!matcherIdlingResource.isMatched()) {
            throw PerformException.Builder()
                .withActionDescription(description)
                .withViewDescription(
                    HumanReadables.getViewHierarchyErrorMessage(
                        view,
                        null,
                        "Action timed out : $description",
                        null
                    )
                )
                .build()
        }
    }

    private fun anyView(): CustomMatcher<View>? {
        return object : CustomMatcher<View>("") {
            override fun matches(item: Any): Boolean {
                return true
            }
        }
    }

}