package com.axa.brastlewark

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.axa.brastlewark.data.BrastlewarkRepository
import com.axa.brastlewark.utils.dummy.gnomeDummy1
import com.axa.brastlewark.model.Gnome
import com.axa.brastlewark.ui.details.GnomeDetailsViewModel
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
class GnomeDetailsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: BrastlewarkRepository

    @Mock
    private lateinit var gnomeObserver: Observer<Event<Gnome>>

    private lateinit var eventGnome: Event<Gnome>
    private lateinit var liveDataEvent: LiveData<Event<Gnome>>

    @Test
    fun `retrieve gnome details from Repository correctly`() {
        eventGnome = Event(Event.Status.SUCCESS, gnomeDummy1, null)
        liveDataEvent = MutableLiveData(eventGnome)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getGnome(0)

            val viewModel = GnomeDetailsViewModel(repository)
            viewModel.gnome.observeForever(gnomeObserver)
            viewModel.start(0)

            verify(repository).getGnome(0)
            verify(gnomeObserver).onChanged(Event.success(gnomeDummy1))

            assertEquals(gnomeDummy1, viewModel.gnome.value?.data)

            viewModel.gnome.removeObserver(gnomeObserver)
        }
    }

    @Test
    fun `error retrieving gnome details from Repository`() {
        val errorMessage = "Error loading gnome"
        eventGnome = Event(Event.Status.ERROR, null, errorMessage)
        liveDataEvent = MutableLiveData(eventGnome)

        testCoroutineRule.runBlockingTest {
            doReturn(liveDataEvent)
                .`when`(repository)
                .getGnome(0)

            val viewModel = GnomeDetailsViewModel(repository)

            viewModel.gnome.observeForever(gnomeObserver)
            viewModel.start(0)

            verify(repository).getGnome(0)
            verify(gnomeObserver).onChanged(Event.error(errorMessage, null))

            assertEquals(errorMessage, viewModel.gnome.value?.message)

            viewModel.gnome.removeObserver(gnomeObserver)
        }
    }
}