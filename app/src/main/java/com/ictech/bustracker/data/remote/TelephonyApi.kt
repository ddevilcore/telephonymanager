package com.ictech.bustracker.data.remote

import com.ictech.bustracker.data.remote.dto.TelephonyApiDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TelephonyApi {
    @POST("api/tracker/cell-data")
    suspend fun postTelephonyData(@Body data: TelephonyApiDto): Any
}