package `in`.org.projecteka.jataayu.provider.ui.fragment

import `in`.org.projecteka.featuresprovider.R.id.*
import `in`.org.projecteka.jataayu.R
import `in`.org.projecteka.jataayu.testUtil.MockServerDispatcher
import `in`.org.projecteka.jataayu.ui.activity.TestsOnlyActivity
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.actions.ClickActions.click
import br.com.concretesolutions.kappuccino.actions.TextActions.typeText
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.notDisplayed
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
        activityRule.activity.addFragment(searchFragment)
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
}

@Test
fun shouldShowNoResultsFoundForInvalidProviderName() {
    typeText("SomeInvalidProvider") { id(sv_provider) }
    displayed {
        id(provider_name)
        text("R.string.no_results_found")
    }
}

@Test
fun shouldClearProviderNameTextOnClearButtonClick() {
    typeText("Some Really Long Provider Name") { id(sv_provider) }
    displayed { id(iv_clear_results) }
    click(true) { id(iv_clear_results) }
    displayed {
        id(tv_search_provider_label)
        text(R.string.search_health_information_provider)
    }
    notDisplayed { id(iv_clear_results) }
}

@Test
fun shouldSelectTheClickedProvider() {
    typeText("Health") { id(sv_provider) }
    recyclerView(rv_search_results) { atPosition(1) { click() } }
    displayed {
        id(tv_selected_provider)
        text("Max Health Care, Bangalore")
    }

    notDisplayed { id(iv_clear_results) }
    notDisplayed { id(sv_provider) }
    displayed {
        id(tv_mobile_number)
        text("98XXXXXX10")
    }
}

@Test
fun shouldClearSelectedProviderOnProviderNameClick() {
    typeText("Health") {
        id(sv_provider)
    }
    recyclerView(rv_search_results) { atPosition(1) { click() } }
    click {
        allOf {
            id(tv_selected_provider)
            text("Max Health Care, Bangalore")
        }
    }
    notDisplayed { id(tv_selected_provider) }
    displayed { id(iv_clear_results) }
    displayed {
        id(sv_provider)
        text("Health")
    }
}
