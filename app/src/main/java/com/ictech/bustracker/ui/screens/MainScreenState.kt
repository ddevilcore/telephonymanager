package com.ictech.bustracker.ui.screens

import com.ictech.bustracker.domain.model.Location

data class MainScreenState(
    val isConnected: Boolean = false,
    val currentLocation: Location? = null,
)