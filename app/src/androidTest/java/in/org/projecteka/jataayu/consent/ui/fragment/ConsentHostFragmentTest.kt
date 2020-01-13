package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.R
import org.junit.Assert.*

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
import org.hamcrest.Matcher.*
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
public class ConsentHostFragmentTest {
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private val consentHostFragment = ConsentHostFragment()

    @Before
    @Throws(Exception::class)
    fun setup() {
        activityRule.activity.addFragment(consentHostFragment)
    }

    @Test
    fun shouldShowRequestAndConsentsTab() {
        displayed {
            parent(R.id.tabs) {
                text("REQUESTS")
                text("CONSENTS")
            }
            allOf {
                id(R.id.lbl_no_new_requests)
                text(R.string.no_new_consent_requests)
            }
        }
    }
}