package com.example.smsSender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smsSender.data.datastore.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(private val settingsRepository: SettingsRepository) :
    ViewModel() {

    val visitText = settingsRepository.userTextFlow

    fun saveText(text: String) {
        viewModelScope.launch {
            settingsRepository.saveUserText(text)
        }
    }
}