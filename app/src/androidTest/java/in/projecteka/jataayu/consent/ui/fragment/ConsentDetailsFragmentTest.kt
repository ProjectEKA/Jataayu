package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.testUtil.AssetReaderUtil
import `in`.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.notDisplayed
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
class ConsentDetailsFragmentTest{

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
        Thread.sleep(5000)

        readConsentAndLaunchFragment("consent_requested.json")
        activityRule.activity.addFragment(RequestedConsentDetailsFragment())
    }

    private fun readConsentAndLaunchFragment(fileName: String) {
        consent =  Gson().fromJson<Consent>(AssetReaderUtil.asset(activityRule.activity.applicationContext, fileName), Consent::class.java)!!

        EventBus.getDefault().postSticky(consent)
    }

    @Test
    fun shouldShowButtonsButtonIfStatusIsRequested(){
        displayed{
            id(R.id.btn_edit)
            text("Edit")
        }

        displayed { id(R.id.btn_grant)
            text("Grant consent")
        }

        displayed {
            id(R.id.btn_deny)
            text("deny")
        }
    }

    @Test
    fun shouldRenderConsentDetails(){
        displayed {
            id(R.id.tv_requester_name)
            text("Dr. Lakshmi")
        }

        displayed {
            id(R.id.tv_requester_organization)
            text("AIMS")
        }

        displayed {
            id(R.id.tv_purpose_of_request)
            text("REMOTE_CONSULTING")
        }

        displayed {
            id(R.id.tv_requests_info_from)
            text("01 Jan, 2020")
        }

        displayed {
            id(R.id.tv_requests_info_to)
            text("08 Jan, 2020")
        }

        displayed {
            id(R.id.tv_expiry)
            text("05 PM, 30 Jan, 3020")
        }

        displayed {
            id(R.id.cg_request_info_types)
            text("Condition")
            text("DiagnosticReport")
            text("Observation")
            }

        displayed { id(R.id.disclaimer)
        text("By granting this consent, you also agree to let Dr. Lakshmi view your health information from all the linked accounts.")}
    }

    @Test
    fun shouldNotShowButtonsForExpiredConsent(){

        readConsentAndLaunchFragment("consent_expired.json")
        activityRule.activity.replaceFragment(RequestedConsentDetailsFragment())

        notDisplayed { id(R.id.btn_edit)
        text("Edit")}

        notDisplayed { id(R.id.btn_grant)
            text("Grant consent")
        }

        notDisplayed {
            id(R.id.btn_deny)
            text("deny")
        }
    }

    @After
    fun tearDown(){
        webServer.shutdown()
    }


}