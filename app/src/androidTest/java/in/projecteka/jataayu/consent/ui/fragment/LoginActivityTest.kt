package `in`.projecteka.jataayu.registration.ui.activity

import `in`.projecteka.jataayu.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.actions.TextActions
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest{
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun shouldRenderUI(){
        VisibilityAssertions.displayed {
            allOf {
                id(R.id.et_username)
                text("")
            }

            allOf {
                id(R.id.tv_provider_name)
                text(R.string.cm_config_provider)
            }

            allOf {
                id(R.id.et_password)
                text("")
            }

            allOf {
                id(R.id.btn_register)
                text("REGISTER")
            }

            allOf {
                id(R.id.btn_login)
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
        TextActions.typeText("username") {
            id(R.id.et_username)
        }
        onView(withId(R.id.btn_login)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldDisableLoginButtonIfPasswordIsEmpty() {
        TextActions.typeText("password") {
            id(R.id.et_password)
        }
        onView(withId(R.id.btn_login)).check(matches(not(isEnabled())))
    }

    @Test
    fun shouldEnableLoginButtonIfUsernameAndPasswordIsNlotEmpty(){
        TextActions.typeText("username") {
            id(R.id.et_username)
        }
        TextActions.typeText("password") {
            id(R.id.et_password)
        }
        onView(withId(R.id.btn_login)).check(matches(isEnabled()))
    }

    @Test
    fun shouldDisableAccountLockLableByDefault(){
        onView(withId(R.id.account_lock_error_text)).check(matches(isEnabled()))
        onView(withId(R.id.divider)).check(matches(isEnabled()))

    }
}
