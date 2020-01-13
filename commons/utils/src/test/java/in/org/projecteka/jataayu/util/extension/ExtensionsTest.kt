package `in`.org.projecteka.jataayu.util.extension

import `in`.org.projecteka.jataayu.util.R
import `in`.org.projecteka.jataayu.util.ui.activity.TestsOnlyActivity
import `in`.org.projecteka.jataayu.util.ui.fragment.TestsOnlyFragment
import android.view.View
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExtensionsTest {

    private lateinit var testActivity: TestsOnlyActivity

    @Before
    fun setUp() {
        testActivity = Mockito.mock(TestsOnlyActivity::class.java)
    }
/*
    @Test
    fun shouldStartActivityWithJustClassName() {
        val clazz = AppCompatActivity::class.java
        testActivity.startActivity(clazz)
        verify(testActivity).startActivity(clazz)
    }

    @Test
    fun shouldStartActivityWithJustClassNameFromAFragment() {
        val dummyFragment = mock(TestsOnlyFragment::class.java)
        `when`(dummyFragment.context).thenReturn(testActivity)
        val clazz = AppCompatActivity::class.java
        dummyFragment.startActivity(clazz)
        verify(testActivity).startActivity(clazz)
    }*/

    @Test
    fun shouldFindViewByIdForGivenContext() {
        val dummyView = mock(View::class.java)
        @IntegerRes val someResource = R.id.textWatcher
        `when`(testActivity.findViewById<View>(anyInt())).thenReturn(dummyView)
        testActivity.findView<View>(someResource)
        verify(testActivity).findViewById<View>(someResource)
    }
}