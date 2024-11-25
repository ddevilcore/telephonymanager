package com.ictech.bustracker.ui.screens

import android.location.Location
import com.ictech.bustracker.domain.model.TelephonyInfo

sealed class MainScreenEvents {
    data class postData(val data: TelephonyInfo): MainScreenEvents()
    data class updateConnectionStatus(val status: Boolean): MainScreenEvents()
    data object getCurrentLocation: MainScreenEvents()
}