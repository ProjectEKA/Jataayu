
import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.presentation.ui.viewmodel.IntroScreensActivityViewModel
import `in`.projecteka.jataayu.util.repository.PreferenceRepository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IntroScreensActivityViewModelTest {

    @Mock
    private lateinit var preferenceRepository: PreferenceRepository

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var introScreensActivityViewModel: IntroScreensActivityViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        introScreensActivityViewModel =
            IntroScreensActivityViewModel(
                preferenceRepository
            )
        introScreensActivityViewModel.initialSetup()
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(preferenceRepository)
        validateMockitoUsage()
    }

    @Test
    fun `should initialize view model with correct values`() {
        assertEquals(4, introScreensActivityViewModel.layouts?.size)

        assertEquals(0, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)

        introScreensActivityViewModel.addBottomDotsEvent.observeForever{
            assert(true)
        }
    }

    @Test
    fun `should select intro first page`(){
        introScreensActivityViewModel.onPageSelected(0)
        assertEquals(0, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(0, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select intro second page`(){
        introScreensActivityViewModel.onPageSelected(1)
        assertEquals(1, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(1, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select intro third page`(){
        introScreensActivityViewModel.onPageSelected(2)
        assertEquals(2, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(2, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select intro fourth page`(){
        introScreensActivityViewModel.onPageSelected(3)
        assertEquals(3, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(3, it)
        }
        assertEquals(R.string.get_started, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should show intro first page initially`(){
        assertEquals(0, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show intro second page on next button click from intro first page`(){
        introScreensActivityViewModel.onNextClick()
        assertEquals(1, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show intro third page on next button click from intro second page`(){
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        assertEquals(2, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show intro fourth page on next button click from intro third page`(){
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        assertEquals(3, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should get started on next button click from intro fourth page`(){
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()

        assertEquals(3, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)

        verify(preferenceRepository, times(1)).shouldShowIntro = false

        introScreensActivityViewModel.getStartedEvent.observeForever{
            assert(true)
        }
    }
}