package com.example.todo.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

class NotificationPermissionState(
    private val permissionStatus: State<PermissionStatus>,
    private val launcher: ManagedActivityResultLauncher<String, Boolean>?
) {
    val status by permissionStatus
    val isGranted: Boolean
        get() = status == PermissionStatus.GRANTED

    fun requestPermission() {
        println("permission requested")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            println("but not really")
            launcher?.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

enum class PermissionStatus {
    GRANTED,
    UNKNOWN,
    CAN_ASK,
    DENIED_PERMANENTLY
}

@Composable
fun rememberNotificationPermissionState(): NotificationPermissionState {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return remember {
            NotificationPermissionState(
                permissionStatus = mutableStateOf(PermissionStatus.GRANTED),
                launcher = null
            )
        }
    }

    val context = LocalContext.current
    val permission = Manifest.permission.POST_NOTIFICATIONS
    val permissionStatus = remember {
        mutableStateOf(
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            )
                PermissionStatus.GRANTED
            else
                PermissionStatus.UNKNOWN
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        permissionStatus.value = when {
            isGranted -> {
                PermissionStatus.GRANTED
            }

            shouldShowRequestPermissionRationale(context as Activity, permission) -> {
                PermissionStatus.CAN_ASK
            }

            else -> PermissionStatus.DENIED_PERMANENTLY
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionStatus.value = PermissionStatus.GRANTED
        } else if (permissionStatus.value == PermissionStatus.GRANTED) {
            permissionStatus.value = PermissionStatus.UNKNOWN
        }
    }

    return remember {
        NotificationPermissionState(
            permissionStatus = permissionStatus,
            launcher = launcher
        )
    }
}