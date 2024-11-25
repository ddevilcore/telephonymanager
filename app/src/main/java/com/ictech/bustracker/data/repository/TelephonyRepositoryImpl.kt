package com.ictech.bustracker.data.repository

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.ictech.bustracker.core.util.Response
import com.ictech.bustracker.data.remote.TelephonyApi
import com.ictech.bustracker.domain.model.TelephonyInfo
import com.ictech.bustracker.domain.repository.TelephonyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelephonyRepositoryImpl @Inject constructor(
    private val telephonyApi: TelephonyApi
) : TelephonyRepository {
    override suspend fun postTelephonyData(data: TelephonyInfo): Flow<Response<Any>> {
        return flow {

            emit(Response.Loading(true))

            val response = try {
                telephonyApi.postTelephonyData(data = data)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Response.Error("Could not post data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Response.Error("Could not post data"))
                null
            }

            if (response == null) {
                emit(Response.Loading(false))
                emit(Response.Error("Null response"))
            }

            response.let { res ->
                println("====== $res =========")
                if (res === null) {
                    emit(Response.Error("Null response"))
                } else emit(Response.Success(data = res))
                emit(Response.Loading(false))
            }
        }
    }
}