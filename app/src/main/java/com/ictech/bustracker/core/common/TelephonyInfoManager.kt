package com.ictech.bustracker.core.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.CellInfo
import android.telephony.CellInfoGsm
import android.telephony.CellInfoWcdma
import android.telephony.CellLocation
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.CellInfoCallback
import androidx.annotation.RequiresApi
import com.ictech.bustracker.domain.model.StretchedCellInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("MissingPermission")
class TelephonyInfoManager(private val context: Context) {
    private val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val _cellInfoList = MutableStateFlow<MutableList<StretchedCellInfo>>(mutableListOf())
    val cellInfoList = _cellInfoList.asStateFlow()

    private fun cellInfoAcc(it: CellInfo) {
//        println("CONTEXT $context")
        var cellList = mutableListOf<StretchedCellInfo>()
        if (it is CellInfoWcdma) {
            _cellInfoList.update { list ->
                cellList.add(
                    StretchedCellInfo(
                        cellId = it.cellIdentity.cid,
                        lac = it.cellIdentity.lac,
                        signalStrength = it.cellSignalStrength,
                        tac = getTAC(),
                        time = Date().toLocaleString()
                    )
                )
                cellList
            }
        } else if (it is CellInfoGsm) {
            _cellInfoList.update { list ->
                cellList.add(
                    StretchedCellInfo(
                        cellId = it.cellIdentity.cid,
                        lac = it.cellIdentity.lac,
                        signalStrength = it.cellSignalStrength,
                        tac = getTAC(),
                        time = Date().toLocaleString()
                    )
                )
                cellList
            }
        }
    }

    fun getCellInfo() {
        manager.requestCellInfoUpdate(context.mainExecutor, object: CellInfoCallback() {
            override fun onCellInfo(activeCellInfo: MutableList<CellInfo>) {
                activeCellInfo.forEach {
                    cellInfoAcc(it)
                }
            }
        })
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