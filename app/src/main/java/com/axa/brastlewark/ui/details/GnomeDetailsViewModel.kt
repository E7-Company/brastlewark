package com.axa.brastlewark.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.axa.brastlewark.data.BrastlewarkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GnomeDetailsViewModel @Inject constructor(
    repository: BrastlewarkRepository
) : ViewModel() {

    private val _id = MutableLiveData<Int>()

    private val _gnome = _id.switchMap { id ->
        repository.getGnome(id)
    }

    val gnome = _gnome

    fun start(id: Int) {
        _id.value = id
    }

}