package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.org.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter.RecyclerViewHolder
import `in`.org.projecteka.jataayu.ui.testUtil.espresso.CustomRecyclerViewMatcher
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@LargeTest
@RunWith(AndroidJUnit4::class)
class ProviderSearchFragmentTest {

    @get:Rule
    var activityRule : IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var webServer : MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.start(8080)

        webServer.dispatcher = MockServerDispatcher().RequestDispatcher(activityRule.activity.applicationContext)

        val searchFragment = ProviderSearchFragment()
        activityRule.activity.addFragment(searchFragment)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun shouldSearchProvidersByGivenName() {
        onView(withId(R.id.sv_provider)).perform(typeText("Health"))
        CustomRecyclerViewMatcher(R.id.rv_search_results).atPositionOnView(1, R.id.provider_name)
            .matches(allOf(withText("Max Health Care, Bangalore"), isDisplayed()))
    }

    @Test
    fun shouldShowNoResultsFoundForInvalidProviderName() {
        onView(withId(R.id.sv_provider)).perform(typeText("SomeInvalidProvider"))
        onView(withId(R.id.provider_name)).check(
            matches(allOf(withText(R.string.no_results_found), isDisplayed())))
    }

    @Test
    fun shouldClearProviderNameTextOnClearButtonClick() {
        onView(withId(R.id.sv_provider)).perform(typeText("Some Really Long Provider Name")).check(
            matches(allOf(withText("Some Really Long Provider Name"), isDisplayed())))
        onView(withId(R.id.iv_clear_results)).check(
            matches(allOf(withEffectiveVisibility(Visibility.VISIBLE), isDisplayed())))
        onView(withId(R.id.iv_clear_results)).perform(click())
        onView(
            allOf(
                withId(R.id.tv_search_provider_label), withText(R.string.search_health_information_provider))).check(
            matches(isDisplayed()))
        onView(withId(R.id.iv_clear_results)).check(
            matches(allOf(withEffectiveVisibility(Visibility.GONE), not(isDisplayed()))))
    }

    @Test
    fun shouldSelectTheClickedProvider() {
        onView(withId(R.id.sv_provider)).perform(typeText("Health"))
        onView(withId(R.id.rv_search_results)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerViewHolder>(
            1, click()))
        waitForId(R.id.tv_selected_provider, 1000)
        onView(allOf(withId(R.id.tv_selected_provider), withText("Max Health Care, Bangalore"))).check(
            matches(allOf(withEffectiveVisibility(Visibility.VISIBLE), isDisplayed())))
        onView(withId(R.id.iv_clear_results)).check(
            matches(allOf(withEffectiveVisibility(Visibility.GONE), not(isDisplayed()))))
        onView(withId(R.id.sv_provider)).check(
            matches(allOf(withEffectiveVisibility(Visibility.GONE), not(isDisplayed()))))
        onView(allOf(withId(R.id.tv_mobile_number), withText("98XXXXXX10"), isDisplayed())).check(matches(isDisplayed()))
    }

    @Test
    fun shouldClearSelectedProviderOnProviderNameClick() {
        onView(withId(R.id.sv_provider)).perform(typeText("Health"))
        onView(withId(R.id.rv_search_results)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerViewHolder>(
            1, click()))
        onView(allOf(withId(R.id.tv_selected_provider), withText("Max Health Care, Bangalore"))).perform(click())
        onView(withId(R.id.tv_selected_provider)).check(
            matches(allOf(withEffectiveVisibility(Visibility.GONE), not(isDisplayed()))))
        onView(withId(R.id.iv_clear_results)).check(
            matches(allOf(withEffectiveVisibility(Visibility.VISIBLE), isDisplayed())))
        onView(withId(R.id.sv_provider)).check(
            matches(allOf(withText("Health"), withEffectiveVisibility(Visibility.VISIBLE), isDisplayed())))
    }
}

fun waitForId(viewId: Int, millis: Long): ViewAction? {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with id <$viewId> during $millis millis."
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + millis
            val viewMatcher: Matcher<View> = withId(viewId)
            do {
                for (child in TreeIterables.breadthFirstViewTraversal(view)) { // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }
                uiController.loopMainThreadForAtLeast(50)
            } while (System.currentTimeMillis() < endTime)
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }
}
