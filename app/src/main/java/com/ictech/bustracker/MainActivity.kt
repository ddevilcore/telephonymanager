package com.ictech.bustracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ictech.bustracker.core.common.TelephonyInfoManager
import com.ictech.bustracker.ui.screens.MainScreen
import com.ictech.bustracker.ui.screens.MainScreenViewModel
import com.ictech.bustracker.ui.theme.BusTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mainScreenViewModel = hiltViewModel<MainScreenViewModel>()
                    mainScreenViewModel.telephonyManager = TelephonyInfoManager(context = LocalContext.current)
                    MainScreen(
                        state = mainScreenViewModel.state,
                        telephonyInfoManager = mainScreenViewModel.telephonyManager) {
                            mainScreenViewModel.onEvent(it)
                    }
                }
            }
        }
    }
}