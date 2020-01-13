package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.R.id.tv_requested_date
import `in`.org.projecteka.jataayu.consent.R.id.*
import `in`.org.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import br.com.concretesolutions.kappuccino.actions.ClickActions.click
import org.hamcrest.CoreMatchers.*

@LargeTest
@RunWith(AndroidJUnit4::class)
public class RequestListFragmentTest {
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var webServer: MockWebServer
    private lateinit var requestListFragment: RequestListFragment

    @Before
    @Throws(Exception::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.start(8080)

        webServer.dispatcher = MockServerDispatcher().RequestDispatcher(activityRule.activity.applicationContext)

        requestListFragment = RequestListFragment()
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
                text(R.string.no_new_consent_requests)
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
    fun shouldRenderConsentRequestItem() {
        Thread.sleep(1000)
        verifyAllRequestsRendered()
    }

    @Test
    fun shouldRenderAllRequests() {
        Thread.sleep(1000)
        click { id(sp_request_filter) }
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("All requests (2)"))).perform(click())
        onView(withId(sp_request_filter)).check(matches(withSpinnerText(containsString("All requests (2)"))))
        verifyAllRequestsRendered()
    }

    private fun verifyAllRequestsRendered() {
        recyclerView(R.id.rvConsents) {
            sizeIs(2)
            atPosition(0) {
                displayed {
                    allOf {
                        id(tv_requested_date)
                        text("Requested Jan 5, 2020")
                    }
                    allOf {
                        id(tv_requester_name)
                        text("Dr. Shruthi Nair")
                    }
                    allOf {
                        id(tv_requester_organization)
                        text("Max Health Care")
                    }
                    allOf {
                        id(tv_purpose_of_request)
                        text("General Consulting")
                    }
                    allOf {
                        id(tv_requests_info_from)
                        text("06/01/19")
                    }
                    allOf {
                        id(tv_requests_info_to)
                        text("06/01/20")
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
