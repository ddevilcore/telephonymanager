package com.ictech.bustracker.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.telephony.CellLocation
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ictech.bustracker.core.common.TelephonyInfoManager
import com.ictech.bustracker.domain.model.Location
import com.ictech.bustracker.domain.model.TelephonyInfo
import kotlinx.coroutines.delay
import java.util.Date

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("PermissionLaunchedDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
    telephonyInfoManager: TelephonyInfoManager,
    onEvent: (MainScreenEvents) -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val time = remember { mutableStateOf("") }
    val tac: MutableState<String?> = remember { mutableStateOf("") }
    val imsi: MutableState<String?> = remember { mutableStateOf("") }
    val cellInfo = remember { telephonyInfoManager.cellInfoList }.collectAsState()

    val shouldShowDialog = remember { mutableStateOf(true) }
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(true){
        locationPermissions.launchMultiplePermissionRequest()
        try {
            while(!state.isConnected) {
                delay(1000)
                telephonyInfoManager.getCellInfo()
                onEvent(MainScreenEvents.getCurrentLocation)
                time.value = Date().toLocaleString()
                imsi.value = telephonyInfoManager.getIMSI()
                tac.value = telephonyInfoManager.getTAC()
                println("cellInfo ${cellInfo.value}")
                onEvent(MainScreenEvents.postData(
                    TelephonyInfo(
                        cellInfo = cellInfo.value,
                        imsi = imsi.value,
                        location = Location(
                            state.currentLocation?.latitude,
                            state.currentLocation?.longitude
                        )
                    )
                ))
            }
        } catch (ex: Exception) {
            println("Got an exception! ${ex.localizedMessage}")
        }
    }


    Scaffold {
        if (shouldShowDialog.value) {
            Dialog(
                onDismissRequest = { shouldShowDialog.value = false },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                )
            ) {
                Box(
                    modifier = Modifier
                        .width(450.dp)
                        .height(450.dp)
                        .background(color = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
//                        println("cellinfo ${cellInfo.value}")
                        Text(
                            text = "LOCATION:${state.currentLocation} \n" +
                                    "IMSI: ${imsi.value}\n" +
                                    "TAC: ${tac.value}\n" +
                                    "CELL INFO: ${cellInfo.value}",
                            modifier = Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(
                                    "LOCATION:${state.currentLocation} \n" +
                                            "IMSI: ${imsi.value}\n" +
                                            "TAC: ${tac.value}\n" +
                                            "CELL INFO: ${cellInfo.value}"
                                ))
                            }
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            onClick = {
                                shouldShowDialog.value = false
                            }
                        ) {
                            Text(text = "Закрыть")
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dotColor = when (state.isConnected) {
                    true -> Color.Green
                    false -> Color.Red
                }
                Box(
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .size(20.dp)
                        .background(color = dotColor, shape = CircleShape),

                    content = {},
                    contentAlignment = Alignment.Center
                )
                val connectedText = when (state.isConnected) {
                    true -> "Подключен"
                    false -> "Отключен"
                }
                Text(
                    modifier = Modifier,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    text = connectedText
                )
            }
            Box(
                modifier = Modifier
                    .padding(all = 25.dp)
                    .size(325.dp)
                    .background(
                        color = Color(red = 106, green = 196, blue = 102),
                        shape = CircleShape,
                    )
                    .clickable { shouldShowDialog.value = true }
            )
        }
    }
}