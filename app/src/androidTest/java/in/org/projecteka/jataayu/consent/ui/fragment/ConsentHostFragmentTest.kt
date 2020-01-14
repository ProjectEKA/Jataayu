package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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