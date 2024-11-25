package com.ictech.bustracker.ui.screens

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ictech.bustracker.core.common.LocationTracker
import com.ictech.bustracker.core.common.TelephonyInfoManager
import com.ictech.bustracker.core.util.Response
import com.ictech.bustracker.domain.model.Location
import com.ictech.bustracker.domain.model.StretchedCellInfo
import com.ictech.bustracker.domain.model.TelephonyInfo
import com.ictech.bustracker.domain.repository.TelephonyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val telephonyRepository: TelephonyRepository,
    private val locationTracker: LocationTracker,
): ViewModel() {
    lateinit var telephonyManager: TelephonyInfoManager
    var state by mutableStateOf(MainScreenState())
        private set

    fun onEvent(event: MainScreenEvents) {
        when (event) {
            is MainScreenEvents.postData -> {
                postData(event.data)
            }

            is MainScreenEvents.updateConnectionStatus -> {
                updateConnectionStatus(event.status)
            }

            is MainScreenEvents.getCurrentLocation -> {
                getCurrentLocation()
            }
        }
    }

    private fun postData(data: TelephonyInfo) {
        viewModelScope.launch {
            telephonyRepository.postTelephonyData(data).collect {
                when (it) {
                    is Response.Success -> {
                        updateConnectionStatus(true)
                    }
                    is Response.Error -> {
                        updateConnectionStatus(false)
                    }

                    is Response.Loading -> {}
                }
            }
        }
    }
    private fun updateConnectionStatus(status: Boolean) {
        state = state.copy(isConnected = status)
    }
    private fun getCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val location = locationTracker.getCurrentLocation()
            state = state.copy(currentLocation = Location(location?.latitude, location?.longitude))
        }
    }
}