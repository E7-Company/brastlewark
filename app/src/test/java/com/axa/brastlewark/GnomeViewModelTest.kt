package com.axa.brastlewark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.axa.brastlewark.data.BrastlewarkRepository
import com.axa.brastlewark.utils.dummy.gnomeDummyList
import com.axa.brastlewark.model.Gnome
import com.axa.brastlewark.ui.gnomes.GnomeViewModel
import com.axa.brastlewark.utils.Event
import com.axa.brastlewark.utils.TestCoroutineRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GnomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: BrastlewarkRepository

    @Mock
    private lateinit var gnomesObserver: Observer<Event<List<Gnome>>>

    private lateinit var eventGnomes: Event<List<Gnome>>
    private lateinit var liveDataEvent: LiveData<Event<List<Gnome>>>

    @Test
    fun `retrieve all gnomes from Repository correctly`() {
        eventGnomes = Event(Event.Status.SUCCESS, gnomeDummyList, null)
        liveDataEvent = MutableLiveData(eventGnomes)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getGnomes()

            val viewModel = GnomeViewModel(repository)
            viewModel.getGnomes().observeForever(gnomesObserver)

            verify(repository).getGnomes()
            verify(gnomesObserver).onChanged(Event.success(gnomeDummyList))

            assertEquals(gnomeDummyList, viewModel.getGnomes().value?.data)

            viewModel.getGnomes().removeObserver(gnomesObserver)
        }
    }

    @Test
    fun `error retrieving gnomes from Repository`() {
        val errorMessage = "Error loading gnomes"
        eventGnomes = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventGnomes)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getGnomes()

            val viewModel = GnomeViewModel(repository)
            viewModel.getGnomes().observeForever(gnomesObserver)

            verify(repository).getGnomes()
            verify(gnomesObserver).onChanged(Event.error(errorMessage, null))

            assertEquals(errorMessage, viewModel.getGnomes().value?.message)

            viewModel.getGnomes().removeObserver(gnomesObserver)
        }
    }

}