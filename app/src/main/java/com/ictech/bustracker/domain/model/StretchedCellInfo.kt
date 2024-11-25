package com.ictech.bustracker.domain.model

import android.telephony.CellIdentity
import android.telephony.CellSignalStrength
import com.google.gson.annotations.SerializedName

data class StretchedCellInfo(
    @SerializedName("cell_id") private val cellId: Int?,
    private val lac: Int?,
    @SerializedName("signal_strength") private val signalStrength: CellSignalStrength,
    private val time: String?,
    private val tac: String?,
)