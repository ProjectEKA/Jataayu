package `in`.org.projecteka.jataayu.consent.ui.fragment

import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.registration.ui.fragment.RegistrationOtpFragment
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.actions.TextActions
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import org.greenrobot.eventbus.EventBus
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegistrationOTPFragmentTest{

    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private val mobileNumber = "9876543210"

    @Before
    fun setUp()
    {
        activityRule.activity.addFragment(RegistrationOtpFragment())
        EventBus.getDefault().postSticky(mobileNumber)
    }

    @Test
    fun shouldRenderUI(){
        displayed{
            allOf{
                id(R.id.lbl_otp_info)
                text("We have sent an SMS with OTP to $mobileNumber")
            }

            allOf {
                id(R.id.et_otp)
                text("")
            }
            allOf {
                id(R.id.btn_continue)
                text("Continue")
            }
        }
    }

    @Test
    fun shouldDisableButtonForOTPisEmpty(){
        Espresso.onView(ViewMatchers.withId(R.id.btn_continue))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun shouldDisableButtonForInvalidOTP(){
        TextActions.typeText("1234") { id(R.id.et_otp) }
        Espresso.onView(ViewMatchers.withId(R.id.btn_continue))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun shouldEnableButtonForValidMobileNumber(){
        TextActions.typeText("123456") { id(R.id.et_otp) }
        Espresso.onView(ViewMatchers.withId(R.id.btn_continue))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }
}