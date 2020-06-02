package `in`.projecteka.jataayu.provider.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.R.id.*
import `in`.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.actions.ClickActions
import br.com.concretesolutions.kappuccino.actions.TextActions.typeText
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProviderSearchFragmentTest {

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

        val searchFragment = ProviderSearchFragment()
        activityRule.activity.addFragment(searchFragment, fragment_container)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        webServer.shutdown()
    }
    @Test
    fun shouldSearchProvidersByGivenName() {
        typeText("Health") { id(sv_provider) }
        recyclerView(rv_search_results) {
            atPosition(1) {
                displayed {
                    id(provider_name)
                    text("Max Health Care, Bangalore")
                }
            }
        }
    }

    @Test
    fun shouldShowNoResultsFoundForInvalidProviderName() {
        typeText("SomeInvalidProvider") { id(sv_provider) }
        displayed {
            id(provider_name)
            text(R.string.no_results_found)
        }
    }

    @Test
    fun shouldClearProviderNameTextOnClearButtonClick() {
        typeText("Some Really Long Provider Name") { id(sv_provider) }
        displayed { id(iv_clear) }
        ClickActions.click { id(iv_clear) }
        displayed {
            id(tv_search_provider_label)
            text(R.string.search_health_information_provider)
        }
        VisibilityAssertions.notDisplayed { id(iv_clear) }
    }

    @Test
    fun shouldSelectTheClickedProvider() {
        typeText("Health") { id(sv_provider) }
        recyclerView(rv_search_results) { atPosition(1) { click() } }
        displayed {
            allOf {
                id(tv_selected_provider)
                text("Max Health Care, Bangalore")
            }
        }

        VisibilityAssertions.notDisplayed { id(sv_provider) }
        displayed {
            id(tv_mobile_number)
        }
    }

    @Test
    fun shouldClearSelectedProviderOnProviderNameClick() {
        typeText("Health") {
            id(sv_provider)
        }
        recyclerView(rv_search_results) { atPosition(1) { click() } }
        ClickActions.click {
            allOf {
                id(tv_selected_provider)
                text("Max Health Care, Bangalore")
            }
        }
        VisibilityAssertions.notDisplayed { id(tv_selected_provider) }
        displayed { id(iv_clear) }
        displayed {
            id(sv_provider)
            text("Health")
        }
    }
}