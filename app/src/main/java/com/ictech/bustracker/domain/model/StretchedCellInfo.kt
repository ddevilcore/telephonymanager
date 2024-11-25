package com.ictech.bustracker.domain.model

import android.telephony.CellIdentity
import android.telephony.CellSignalStrength

data class StretchedCellInfo(
    val cellId: Int?,
    val lac: Int?,
    val signalStrength: CellSignalStrength,
    val time: String,
    val tac: String?,
)