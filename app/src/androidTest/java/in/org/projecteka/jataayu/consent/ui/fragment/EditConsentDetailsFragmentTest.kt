package `in`.org.projecteka.jataayu.consent.ui.fragment

import NestedScrollAction
import `in`.org.projecteka.jataayu.R.id.*
import `in`.org.projecteka.jataayu.core.model.Consent
import `in`.org.projecteka.jataayu.core.model.HiType
import `in`.org.projecteka.jataayu.core.model.Links
import `in`.org.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.org.projecteka.jataayu.testUtil.AssetReaderUtil
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import `in`.org.projecteka.jataayu.util.extension.fromJson
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
import org.greenrobot.eventbus.EventBus
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

    private lateinit var linkedAccounts: List<Links>

    @Before
    @Throws(Exception::class)
    fun setup() {

        consent = Gson().fromJson(AssetReaderUtil.asset(
            activityRule.activity.applicationContext,
            "consent_requested.json"
        ))


        EventBus.getDefault().postSticky(consent)

        var hiTypes = ArrayList<HiType>()

        for (hiType in consent.hiTypes) {
            hiTypes.add(HiType(hiType, true))
        }

        linkedAccounts = Gson().fromJson(AssetReaderUtil.asset(
            activityRule.activity.applicationContext,
            "links.json"
        ))

        linkedAccounts.forEach { link -> link.careContexts.forEach { it.contextChecked = true } }
        EventBus.getDefault().postSticky(HiTypeAndLinks(hiTypes, linkedAccounts))

        val editConsentDetailsFragment = EditConsentDetailsFragment()
        activityRule.activity.addFragment(editConsentDetailsFragment)
    }

    @Test
    fun shouldRenderConsentDetails() {
        Thread.sleep(7000)
        displayed {
            id(tv_requester_name)
            text("Dr. Lakshmi")
        }

        displayed {
            id(tv_requester_organization)
            text("AIMS")
        }

        displayed {
            id(tv_purpose_of_request)
            text("REMOTE_CONSULTING")
        }

        displayed {
            id(tv_requests_info_from)
            text("01 Jan, 2020")
        }

        displayed {
            id(tv_requests_info_to)
            text("08 Jan, 2020")
        }

        onView(withId(tv_expiry_date)).perform(nestedScrollTo())

        displayed {
            id(tv_expiry_date)
            text("30 Jan, 2020")
        }

        onView(withId(tv_expiry_time)).perform(nestedScrollTo())

        displayed {
            id(tv_expiry_time)
            text("05:25 PM")
        }

        onView(withId(cg_request_info_types)).perform(nestedScrollTo())

        displayed {
            id(cg_request_info_types)
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
        Thread.sleep(1000)

        onView(withId(cb_link_all_providers)).check(
            matches(
                isChecked()
            )
        )

        onView(withId(rvLinkedAccounts)).perform(nestedScrollTo())

        recyclerView(rvLinkedAccounts) {
            sizeIs(3)

            atPosition(0) {
                displayed {
                    id(tv_provider_name)
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
}