package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.R.id.btn_continue
import `in`.projecteka.jataayu.R.id.tie_mobile_number
import `in`.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.actions.TextActions.typeText
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationFragmentTest{
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    @Before
    @Throws(Exception::class)
    fun setup() {
        activityRule.activity.addFragment(RegistrationFragment(), R.id.fragment_container)
    }

    @Test
    fun shouldRenderUI(){
        displayed{
            allOf {
                id(tie_mobile_number)
                text("")
            }

            allOf {
                id(btn_continue)
                text("Send OTP")
            }
        }
    }

    @Test
    fun shouldDisableButtonForEmptyMobileNumber(){
        onView(withId(btn_continue)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldDisableButtonForInvalidMobileNumberEntered(){
        typeText("12345"){ id(tie_mobile_number) }
        onView(withId(btn_continue)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldEnableButtonForValidMobileNumber(){
        typeText("1234567890"){ id(R.id.tie_mobile_number) }
        onView(withId(btn_continue)).check(matches(isEnabled()))
    }
}