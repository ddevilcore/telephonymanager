package com.ictech.bustracker.core.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellIdentityGsm
import android.telephony.CellIdentityWcdma
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoWcdma
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.CellInfoCallback
import androidx.annotation.RequiresApi
import com.ictech.bustracker.domain.model.StretchedCellInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.Date

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("MissingPermission")
class TelephonyInfoManager(val context: Context) {
    private val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val sharedFlow = MutableSharedFlow<MutableList<StretchedCellInfo>>()

    private fun cellInfoAcc(list: MutableList<StretchedCellInfo>, it: CellInfo): MutableList<StretchedCellInfo> {
        if (it is CellInfoWcdma) {
            list.add(
                StretchedCellInfo(
                    cellId = it.cellIdentity.cid,
                    lac = it.cellIdentity.lac,
                    signalStrength = it.cellSignalStrength,
                    tac = getTAC(),
                    time = Date().toLocaleString()
                )
            )
        } else if (it is CellInfoGsm) {
            list.add(
                StretchedCellInfo(
                    cellId = it.cellIdentity.cid,
                    lac = it.cellIdentity.lac,
                    signalStrength = it.cellSignalStrength,
                    tac = getTAC(),
                    time = Date().toLocaleString()
                )
            )
        }
        return list
    }

    fun getCellInfo(): MutableList<StretchedCellInfo> {
        var cellInfo: MutableList<StretchedCellInfo> = mutableListOf()
        manager.requestCellInfoUpdate(context.mainExecutor, object: CellInfoCallback() {
            override fun onCellInfo(activeCellInfo: MutableList<CellInfo>) {
                activeCellInfo.forEach {
                    cellInfo = cellInfoAcc(cellInfo, it)
                }
            }
        })
//        sharedFlow.emit(cellInfo)
        return if (cellInfo.size > 0) {
            cellInfo
        } else {
            manager.allCellInfo.forEach {
                cellInfoAcc(cellInfo, it)
            }
            cellInfo
        }
    }

    fun getCellLocation(): CellLocation {
        return manager.cellLocation
    }

    fun getIMSI(): String? {
        return try {
            manager.subscriberId
        } catch (ex: Exception) {
            null
        } catch (ex: Exception) {
            null
        }
    }

    fun getTAC(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            manager.typeAllocationCode
        } else {
            null
        }
    }
}