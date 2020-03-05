package `in`.projecteka.jataayu.consent.ui.fragment

import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.ui.activity.TestsOnlyActivity
import `in`.projecteka.jataayu.user.account.ui.fragment.CreateAccountFragment
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.runner.AndroidJUnit4
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateAccountFragmentTest{
    @get:Rule
    var activityRule: IntentsTestRule<TestsOnlyActivity> =
        IntentsTestRule(TestsOnlyActivity::class.java, true, true)

    @Before
    @Throws(Exception::class)
    fun setup() {
        activityRule.activity.addFragment(CreateAccountFragment())
    }

    @Test
    fun shouldRenderUI(){
        displayed {
            allOf {
                id(R.id.lbl_title)
                text("Creating your account would take less than a minute")
            }

            allOf {
                id(R.id.lbl_username)
                text("Account details")
            }

            allOf {
                id(R.id.lbl_create_username)
                text("Create Username")
            }

            id(R.id.et_username)

            allOf {
                id(R.id.tv_provider_name)
                text("@ncg")
            }

            allOf {
                id(R.id.lbl_password)
                text("Create Password")
            }

            allOf {
                id(R.id.btn_show_hide_password)
                text("Show")
            }


            allOf {
                id(R.id.lbl_patient_details)
                text("Patient details")
            }


            allOf {
                id(R.id.lbl_first_name)
                text("First name")
            }

            id(R.id.et_first_name)


            allOf {
                id(R.id.lbl_last_name)
                text("Last name")
            }

            id(R.id.et_last_name)


            allOf {
                id(R.id.lbl_gender)
                text("Gender")
            }

            id(R.id.cg_gender)

            allOf {
                id(R.id.chip_male)
                text("Male")
            }

            allOf {
                id(R.id.chip_female)
                text("Female")
            }

            allOf {
                id(R.id.chip_others)
                text("Others")
            }

            allOf {
                id(R.id.btn_register)
                text("Register")
            }

        }
    }
}