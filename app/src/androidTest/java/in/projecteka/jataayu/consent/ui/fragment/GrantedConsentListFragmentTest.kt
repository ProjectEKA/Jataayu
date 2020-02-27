package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.consent.R.id.*
import `in`.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import br.com.concretesolutions.kappuccino.actions.ClickActions.click as KClick

@LargeTest
@RunWith(AndroidJUnit4::class)
public class GrantedConsentListFragmentTest {
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var webServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.start(8080)
        webServer.dispatcher = MockServerDispatcher().RequestDispatcher(activityRule.activity.applicationContext)

        val requestListFragment = GrantedConsentListFragment()
        activityRule.activity.addFragment(requestListFragment)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun shouldShowNoRequestsScreenWhenThereAreNoRequests() {
        displayed {
            allOf {
                id(lbl_no_new_requests)
                text("No granted consents")
            }
            allOf {
                id(progressbar_text)
                text(R.string.loading_requests)
            }
            allOf {
                id(progressbar_container)
                background(R.color.progressbar_dimmed_bg)
            }
            id(progress_bar)
        }
    }

        @Test
        fun shouldRenderGrantedConsents() {
            Thread.sleep(8000)
            verifyGrantedConsentsRendered(1)
        }

    @Test
    fun shouldRenderGratedConsents() {
        Thread.sleep(8000)
        KClick { id(sp_request_filter) }
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Active consents (1)"))).perform(click())
        onView(withId(sp_request_filter)).check(matches(withSpinnerText(containsString("Active consents (1)"))))
        verifyGrantedConsentsRendered(1)
    }

    @Test
    fun shouldRenderAllGrantedConsents() {
        Thread.sleep(8000)
        KClick { id(sp_request_filter) }
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("All Consents (2)"))).perform(click())
        onView(withId(sp_request_filter)).check(matches(withSpinnerText(containsString("All Consents (2)"))))
        verifyGrantedConsentsRendered(2)
    }
    @Test
    fun shouldRenderExpiredGrantedConsents() {
        Thread.sleep(10000)
        KClick { id(sp_request_filter) }
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Expired consents (1)"))).perform(click())
        onView(withId(sp_request_filter)).check(matches(withSpinnerText(containsString("Expired consents (1)"))))
        verifyExpiredGrantedConsentsRendered()
    }

    private fun verifyGrantedConsentsRendered(expectedCount: Int) {
        recyclerView(R.id.rvConsents) {
            sizeIs(expectedCount)
            atPosition(0) {
                displayed {
                    allOf {
                        id(tv_requested_date)
                        text("Granted Jan 27, 2020")
                    }
                    allOf {
                        id(tv_requester_name)
                        text("Dr. Lakshmi")
                    }
                    allOf {
                        id(tv_requester_organization)
                        text("Max Health Care")
                    }
                    allOf {
                        id(tv_purpose_of_request)
                        text("REMOTE_CONSULTING")
                    }
                    allOf {
                        id(tv_requests_info_from)
                        text("01 Jan, 2020")
                    }
                    allOf {
                        id(tv_requests_info_to)
                        text("08 Jan, 2020")
                    }
                    allOf {
                        id(seperator)
                        background(R.color.black)
                    }
                    image(R.drawable.ic_arrow_right)
                }
            }
        }
    }

    private fun verifyExpiredGrantedConsentsRendered() {
        recyclerView(R.id.rvConsents) {
            sizeIs(1)
            atPosition(0) {
                displayed {
                    allOf {
                        id(tv_requested_date)
                        text("Granted Jan 27, 2020")
                    }
                    allOf {
                        id(tv_requester_name)
                        text("Dr. Lakshmi")
                    }
                    allOf {
                        id(tv_requester_organization)
                        text("Max Health Care")
                    }
                    allOf {
                        id(tv_purpose_of_request)
                        text("REMOTE_CONSULTING")
                    }
                    allOf {
                        id(tv_requests_info_from)
                        text("01 Jan, 2020")
                    }
                    allOf {
                        id(tv_requests_info_to)
                        text("08 Jan, 2020")
                    }
                    allOf {
                        id(seperator)
                        background(R.color.black)
                    }
                    image(R.drawable.ic_arrow_right)
                }
            }
        }
    }
}
