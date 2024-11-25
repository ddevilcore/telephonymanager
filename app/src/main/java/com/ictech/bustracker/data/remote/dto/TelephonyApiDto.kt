package com.ictech.bustracker.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.ictech.bustracker.domain.model.Location
import com.ictech.bustracker.domain.model.StretchedCellInfo

data class TelephonyApiDto(
    private val items: MutableList<StretchedCellInfo>,
    private val imsi: String?,
    private val location: Location?,
)