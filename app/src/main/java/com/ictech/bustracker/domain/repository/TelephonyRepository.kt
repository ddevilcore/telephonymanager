package com.ictech.bustracker.domain.repository

import com.ictech.bustracker.core.util.Response
import com.ictech.bustracker.domain.model.TelephonyInfo
import kotlinx.coroutines.flow.Flow

interface TelephonyRepository {
    suspend fun postTelephonyData(
        data: TelephonyInfo
    ): Flow<Response<Any>>
}