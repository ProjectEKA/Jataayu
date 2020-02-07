package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.R.id.*
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationFragment
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
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
        activityRule.activity.addFragment(RegistrationFragment())
    }

    @Test
    fun shouldRenderUI(){
        displayed{
            allOf {
                id(et_mobile_number)
                text("")
            }

            allOf {
                id(btn_continue)
                text("CONTINUE")
            }
        }
    }

    @Test
    fun shouldDisplayCountryCode(){
        displayed {
            id(tv_country_code)
            text("+91 - ")
        }
    }

    @Test
    fun shouldDisableButtonForEmptyMobileNumber(){
        onView(withId(btn_continue)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldDisableButtonForInvalidMobileNumberEntered(){
        typeText("12345"){ id(et_mobile_number) }
        onView(withId(btn_continue)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldEnableButtonForValidMobileNumber(){
        typeText("1234567890"){ id(R.id.et_mobile_number) }
        onView(withId(btn_continue)).check(matches(isEnabled()))
    }
}