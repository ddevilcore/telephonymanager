package com.ictech.bustracker.core.common

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}