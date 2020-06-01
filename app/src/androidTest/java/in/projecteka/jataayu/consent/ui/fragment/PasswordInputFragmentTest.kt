package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.registration.ui.fragment.PasswordInputFragment
import `in`.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.actions.TextActions
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
public class PasswordInputFragmentTest {
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    private lateinit var webServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup() {
        webServer = MockWebServer()
        webServer.start(8080)
        webServer.dispatcher = MockServerDispatcher().RequestDispatcher(activityRule.activity.applicationContext)

        val passwordInputFragment = PasswordInputFragment()
        activityRule.activity.addFragment(passwordInputFragment, R.id.fragment_container)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }
    @Test
    fun shouldRenderUI(){
        VisibilityAssertions.displayed {

            allOf {
                id(R.id.et_password)
                text("")
            }

            allOf {
                id(R.id.btn_login)
                text("LOGIN")
            }
        }
    }

    @Test
    fun shouldDisableLoginButtonIfPasswordIsEmpty() {
        TextActions.typeText("") {
            id(R.id.et_password)
        }
        Espresso.onView(ViewMatchers.withId(R.id.btn_login))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }

    @Test
    fun shouldEnableLoginButtonIfPasswordIsNotEmpty(){
        TextActions.typeText("password") {
            id(R.id.et_password)
        }
        Espresso.onView(ViewMatchers.withId(R.id.btn_login))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    @Test
    fun shouldDisableAccountLockLableByDefault(){
        Espresso.onView(ViewMatchers.withId(R.id.account_lock_error_text))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.divider))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))

    }
}
