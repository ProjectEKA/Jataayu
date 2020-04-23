package `in`.projecteka.jataayu.consent.viewmodel

import `in`.projecteka.jataayu.consent.repository.ConsentRepository
import `in`.projecteka.jataayu.core.model.*
import `in`.projecteka.jataayu.core.model.approveconsent.HiTypeAndLinks
import `in`.projecteka.jataayu.util.ui.DateTimeUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import junit.framework.Assert.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class EditConsentDetailsVMTest {

    @Mock
    lateinit var consent: Consent
    @Mock
    lateinit var modifiedConsent: Consent

    @Mock
    lateinit var onSaveClickedEventObserver: Observer<Consent>

    @Mock
    lateinit var onDateClickedEventObserver: Observer<Pair<Consent, Int>>

    @Mock
    lateinit var onTimePickerClickedObserver: Observer<String>

    @Mock
    lateinit var careContext: CareContext

    @Mock
    private lateinit var repository: ConsentRepository

    @Mock
    lateinit var hiType: HiType

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var editConsentDetailsVM: EditConsentDetailsVM

    val careContexts = arrayListOf<CareContext>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        editConsentDetailsVM = EditConsentDetailsVM(repository)
        editConsentDetailsVM.originalConsent = consent
        careContexts.apply { add(careContext) }

        val links = ArrayList<Links>()
        val mockLink = Links(Hip("some_id", "some name"), "some reference id", "some display", careContexts)
        links.add(mockLink)

        val hiTypes = ArrayList<HiType>()
        hiTypes.add(hiType)

        `when`(consent.clone()).thenReturn(modifiedConsent)

        editConsentDetailsVM.onSaveClicked.observeForever(onSaveClickedEventObserver)
        editConsentDetailsVM.onDatePickerClicked.observeForever(onDateClickedEventObserver)
        editConsentDetailsVM.onTimePickerClicked.observeForever(onTimePickerClickedObserver)

        editConsentDetailsVM.setup(consent)
        editConsentDetailsVM.updateHITypesAndLinksReceived(hiTypeAndLinks = HiTypeAndLinks(hiTypes, links))

        editConsentDetailsVM.fromDateLabel.set("11 Apr, 2020")

    }

    @After
    fun tearDown() {
        editConsentDetailsVM.onSaveClicked.removeObserver(onSaveClickedEventObserver)
        editConsentDetailsVM.onDatePickerClicked.removeObserver(onDateClickedEventObserver)
        editConsentDetailsVM.onTimePickerClicked.observeForever(onTimePickerClickedObserver)

    }

    @Test
    fun `try to save a consent when no item is clicked`() {

        editConsentDetailsVM.onClickSave()

        verify(onSaveClickedEventObserver, never()).onChanged(consent)
    }

    @Test
    fun `save enabled when edit start date to a different date, label changes`() {

        val clickedDate = DateTimeUtils.getDate("2020-04-05T05:00:00.000Z")!!
        `when`(modifiedConsent.getPermissionStartDate()).thenReturn("05 Apr, 2020")
        `when`(careContext.contextChecked).thenReturn(true)

        editConsentDetailsVM.onDateClicked(EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID)
        editConsentDetailsVM.onDateSelected(EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID, clickedDate)

        verify(onDateClickedEventObserver, times(1)).onChanged(Pair(modifiedConsent, EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID))
        assertEquals("05 Apr, 2020", editConsentDetailsVM.fromDateLabel.get())
        assertTrue(editConsentDetailsVM.saveEnabled.get())

    }

    @Test
    fun `save disabled when edit start date to a different date, canceling, label does not change`() {

        editConsentDetailsVM.onDateClicked(EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID)

        verify(onDateClickedEventObserver, times(1)).onChanged(Pair(modifiedConsent, EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID))
        Assert.assertEquals("11 Apr, 2020", editConsentDetailsVM.fromDateLabel.get())
        assertFalse(editConsentDetailsVM.saveEnabled.get())
    }

    @Test
    fun `save disabled when edits a date, but no care contexts are checked true`() {
        val clickedDate = DateTimeUtils.getDate("2020-04-05T05:00:00.000Z")!!
        `when`(modifiedConsent.getPermissionStartDate()).thenReturn("05 Apr, 2020")
        `when`(careContext.contextChecked).thenReturn(false)

        editConsentDetailsVM.onDateSelected(EditConsentDetailsVM.FROM_DATE_DATEPICKER_ID, clickedDate)

        assertFalse(editConsentDetailsVM.saveEnabled.get())
    }

    @Test
    fun `save disabled when no provider is checked`() {
        `when`(careContext.contextChecked).thenReturn(false)

        editConsentDetailsVM.checkEditValid()

        assertFalse(editConsentDetailsVM.saveEnabled.get())
    }

    @Test
    fun `save disabled when at least provider is checked`() {
        `when`(careContext.contextChecked).thenReturn(true)
        careContexts.add(mock(CareContext::class.java))

        editConsentDetailsVM.checkEditValid()

        assertTrue(editConsentDetailsVM.saveEnabled.get())
    }

    @Test
    fun `all providers toggle unchecked when only one provider is checked`() {
        `when`(careContext.contextChecked).thenReturn(true)
        careContexts.add(mock(CareContext::class.java))

        editConsentDetailsVM.checkEditValid()

        assertFalse(editConsentDetailsVM.allProvidersChecked.get())
    }


    @Test
    fun `save enabled when all providers checkbox is toggled to true`() {
        val careContext1 = CareContext("", "").apply { contextChecked = false }
        val careContext2 = CareContext("", "").apply { contextChecked = false }
        val links = Links(Hip("some_id", "some name"), "some reference id", "some display", listOf(careContext1, careContext2))

        val hiTypeAndLinks = HiTypeAndLinks(arrayListOf(hiType), arrayListOf(links))
        editConsentDetailsVM.updateHITypesAndLinksReceived(hiTypeAndLinks)
        editConsentDetailsVM.allProvidersChecked.set(false)

        editConsentDetailsVM.toggleProvidersSelection()

        assertTrue(editConsentDetailsVM.allProvidersChecked.get())
    }

    @Test
    fun `save enabled when changing time of expiry`() {
        editConsentDetailsVM.expiryTimeLabel.set("9 AM")
        val permission = mock(Permission::class.java)
        `when`(permission.dataEraseAt).thenReturn("2020-04-11T11:00:00.000Z")
        `when`(modifiedConsent.permission).thenReturn(permission)
        `when`(modifiedConsent.getConsentExpiryTime()).thenReturn("11 AM")
        `when`(careContext.contextChecked).thenReturn(true)


        val pickedTime = Pair(11,0)
        editConsentDetailsVM.onTimePickerClick()
        editConsentDetailsVM.onTimeSelected(pickedTime)

        verify(onTimePickerClickedObserver, times(1)).onChanged(ArgumentMatchers.anyString())
        assertEquals("11 AM", editConsentDetailsVM.expiryTimeLabel.get())
        assertTrue(editConsentDetailsVM.saveEnabled.get())
    }
}