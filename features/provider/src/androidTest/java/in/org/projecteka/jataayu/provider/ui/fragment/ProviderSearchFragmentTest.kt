package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R
import `in`.org.projecteka.jataayu.provider.CustomRecyclerViewMatcher
import `in`.org.projecteka.jataayu.provider.MockServerDispatcher
import `in`.org.projecteka.jataayu.provider.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ProviderSearchFragmentTest {

    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var webServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.start(8080)

        webServer.dispatcher = MockServerDispatcher()
            .RequestDispatcher()

        val searchFragment = ProviderSearchFragment()
        activityRule.activity.addFragment(searchFragment)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun shouldRenderProviderSearchLayout() {
        onView(allOf(withId(R.id.sv_provider), withHint(R.string.search_by_provider_name))).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldSearchProvidersByGivenName() {
        onView(withId(R.id.sv_provider)).perform(typeText("Max"))
        CustomRecyclerViewMatcher(R.id.rv_search_results).atPositionOnView(0, R.id.provider_name)
            .matches(
                allOf(withText("Max Health Care, Bangalore"), isDisplayed())
            )
    }

    @Test
    fun shouldShowNoResultsFoundForInvalidProviderName() {
        onView(withId(R.id.sv_provider)).perform(typeText("SomeInvalidProvider"))
        CustomRecyclerViewMatcher(R.id.rv_search_results).atPositionOnView(0, R.id.provider_name)
            .matches(
                allOf(withText(R.string.no_results_found), isDisplayed())
            )
    }

    @Test
    fun shouldClearProviderNameTextOnClearButtonClick() {
        onView(withId(R.id.sv_provider)).perform(typeText("Some Really Long Provider Name"))
            .check(
                matches(allOf(withText("Some Really Long Provider Name"), isDisplayed()))
            )
        onView(withId(R.id.iv_clear_results)).perform(click())
        onView(
            allOf(
                withId(R.id.sv_provider),
                withText(""),
                withHint(R.string.search_by_provider_name)
            )
        ).check(
            matches(isDisplayed())
        )
    }
}