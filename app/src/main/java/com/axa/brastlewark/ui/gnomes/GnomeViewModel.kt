package com.axa.brastlewark.ui.gnomes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axa.brastlewark.data.BrastlewarkRepository
import com.axa.brastlewark.model.Gnome
import com.axa.brastlewark.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GnomeViewModel @Inject constructor(
    private val repository: BrastlewarkRepository
) : ViewModel() {

    private var gnomesList: LiveData<Event<List<Gnome>>> = MutableLiveData()

    init {
        start()
    }

    fun getGnomes(): LiveData<Event<List<Gnome>>> {
        return gnomesList
    }

    fun start() {
        gnomesList = repository.getGnomes()
    }

}