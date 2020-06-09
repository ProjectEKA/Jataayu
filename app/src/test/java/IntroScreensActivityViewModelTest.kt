
import `in`.projecteka.jataayu.R
import `in`.projecteka.jataayu.presentation.IntroScreensActivityViewModel
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
        introScreensActivityViewModel = IntroScreensActivityViewModel(preferenceRepository)
        introScreensActivityViewModel.init()
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
    fun `should select first page`(){
        introScreensActivityViewModel.onPageSelected(0)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(0, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select second page`(){
        introScreensActivityViewModel.onPageSelected(1)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(1, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select third page`(){
        introScreensActivityViewModel.onPageSelected(2)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(2, it)
        }
        assertEquals(R.string.next, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should select fourth page`(){
        introScreensActivityViewModel.onPageSelected(3)
        introScreensActivityViewModel.addBottomDotsEvent.observeForever {
            assertEquals(3, it)
        }
        assertEquals(R.string.get_started, introScreensActivityViewModel.btnText.get())
    }

    @Test
    fun `should show first page initially`(){
        assertEquals(0, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show second page on next button click from first page`(){
        introScreensActivityViewModel.onNextClick()
        assertEquals(1, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show third page on next button click from second page`(){
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        assertEquals(2, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should show fourth page on next button click from third page`(){
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        introScreensActivityViewModel.onNextClick()
        assertEquals(3, introScreensActivityViewModel.setViewpagerCurrentItemEvent.value)
    }

    @Test
    fun `should get started on next button click from fourth page`(){
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