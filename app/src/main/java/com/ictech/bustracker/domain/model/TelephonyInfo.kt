package com.ictech.bustracker.domain.model

import android.telephony.CellInfo

data class TelephonyInfo(
    val cellInfo: MutableList<StretchedCellInfo>,
    val imsi: String?,
    val location: Location?,
)
