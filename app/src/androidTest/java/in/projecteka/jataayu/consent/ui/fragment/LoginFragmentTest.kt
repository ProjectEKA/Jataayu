package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.R.id.*
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

//TODO: REWRITE TO USE LOGIN ACTIVITY
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest{
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    @Before
    @Throws(Exception::class)
    fun setup() {
//        activityRule.activity.addFragment(LoginFragment())
    }

    @Test
    fun shouldRenderUI(){
        displayed{
            allOf {
                id(et_username)
                text("")
            }

            allOf {
                id(et_password)
                text("")
            }

            allOf {
                id(btn_register)
                text("REGISTER")
            }

            allOf {
                id(btn_login)
                text("LOGIN")
            }
        }
    }

    @Test
    fun shouldDisableLoginButtonIfUsernameAndPasswordIsEmpty(){
        onView(withId(R.id.btn_login)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldDisableLoginButtonIfUsernameIsEmpty(){
        typeText("password"){
            id(R.id.et_password)
        }
        onView(withId(R.id.btn_login)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldDisableLoginButtonIfPasswordIsEmpty() {
        typeText("username") {
            id(R.id.et_username)
        }

    }

    @Test
    fun shouldEnableLoginButtonIfUsernameAndPasswordIsNlotEmpty(){
        typeText("username"){
            id(R.id.et_username)
        }
        typeText("password"){
            id(R.id.et_password)
        }
        onView(withId(R.id.btn_login)).check(matches(isEnabled()))
    }
}