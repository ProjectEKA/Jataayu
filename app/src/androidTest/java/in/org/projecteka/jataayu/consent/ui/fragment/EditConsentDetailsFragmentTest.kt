package `in`.org.projecteka.jataayu.consent.ui.fragment

import NestedScrollAction
import `in`.org.projecteka.jataayu.R.id.*
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.HiType
import `in`.org.projecteka.jataayu.testUtil.AssetReaderUtil
import `in`.org.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import com.google.gson.Gson
import okhttp3.mockwebserver.MockWebServer
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class EditConsentDetailsFragmentTest {

    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var consent: Consent

    private lateinit var eventBus: EventBus

    private lateinit var webServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup() {

        webServer = MockWebServer()
        webServer.start(8080)

        webServer.dispatcher =
            MockServerDispatcher().RequestDispatcher(activityRule.activity.applicationContext)
        Thread.sleep(4000)

        consent = Gson().fromJson<Consent>(
            AssetReaderUtil.asset(
                activityRule.activity.applicationContext,
                "consent_requested.json"
            ), Consent::class.java
        )!!

        EventBus.getDefault().postSticky(consent)

        var hiTypes = ArrayList<HiType>()

        for (hiType in consent.hiTypes) {
            hiTypes.add(HiType(hiType, true))
        }

        EventBus.getDefault().postSticky(hiTypes)

        val editConsentDetailsFragment = EditConsentDetailsFragment()
        activityRule.activity.addFragment(editConsentDetailsFragment)
    }

    @Test
    fun shouldRenderConsentDetails() {
        displayed {
            id(tv_requester_name)
            text("Dr. Lakshmi")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_requester_organization)
            text("Max Health Care")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_purpose_of_request)
            text("REMOTE_CONSULTING")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_requests_info_from)
            text("01 Jan, 2020")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_requests_info_to)
            text("08 Jan, 2020")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_expiry_date)
            text("30 Jan, 2020")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.tv_expiry_time)
            text("05:25 PM")
        }

        displayed {
            id(`in`.org.projecteka.jataayu.R.id.cg_request_info_types)
            text("Condition")
            text("DiagnosticReport")
            text("Observation")
        }
        onView(withText("Condition")).check(matches(isChecked()))
        onView(withText("DiagnosticReport")).check(matches(isChecked()))
        onView(withText("Observation")).check(matches(isChecked()))
    }

    @Test
    fun shouldRenderLinkedAccounts() {
        onView(withId(`in`.org.projecteka.jataayu.R.id.cb_link_all_providers)).check(
            matches(
                isChecked()
            )
        )

        Thread.sleep(1000)

        onView(withId(`in`.org.projecteka.jataayu.R.id.rvLinkedAccounts)).perform(nestedScrollTo())

        recyclerView(`in`.org.projecteka.jataayu.R.id.rvLinkedAccounts) {
            sizeIs(5)

            atPosition(0) {
                displayed {
                    id(`in`.org.projecteka.jataayu.R.id.tv_provider_name)
                    text("Max Health Care")
                }
            }

            atPosition(1) {
                displayed {
                    allOf {
                        id(tv_reference_number)
                        text("131")
                    }
                    allOf {
                        id(tv_patient_name)
                        text("National Cancer program")
                    }
                    allOf {
                        id(cb_care_context)
                        custom(isChecked())

                    }
                }

            }
        }

    }

    private fun nestedScrollTo(): ViewAction? {
        return NestedScrollAction()
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }


}