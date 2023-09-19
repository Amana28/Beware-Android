package com.example.beware.ui.screen.report

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beware.R
import com.example.beware.data.ReportType
import com.example.beware.ui.screen.profile.ProfileTopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onReportIncidentSuccess: () -> Unit = {},
    reportIncidentViewModel: ReportIncidentViewModel = viewModel()
) {

    var reportType by remember { mutableStateOf("") }
    var reportLat by remember { mutableStateOf("") }
    var reportLong by remember { mutableStateOf("") }
    var reportDate by remember { mutableStateOf("") }
    var reportTime by remember { mutableStateOf("") }
    var reportDescription by remember { mutableStateOf("") }


    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val context = LocalContext.current
    var hasImage by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )


    Scaffold(
        topBar = { ReportPageTopBar(title = "Report Incident")},
        containerColor = colorResource(id = R.color.dark_grey)
    ) {contentPadding ->
        Column(modifier = Modifier.padding(contentPadding).padding(30.dp)) {
            OutlinedTextField(value = reportType,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Type") },
                onValueChange = {
                    reportType = it
                },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    focusedTrailingIconColor = Color.White
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                OutlinedTextField(value = reportLat,
                    modifier = Modifier.fillMaxWidth(0.45f),
                    label = { Text(text = "Lat") },
                    onValueChange = {
                        reportLat = it
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.DarkGray,
                        focusedTrailingIconColor = Color.White
                    )
                )


                OutlinedTextField(value = reportLong,
                    modifier = Modifier.fillMaxWidth(0.82f),
                    label = { Text(text = "Long") },
                    onValueChange = {
                        reportLong = it
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color.White,
                        unfocusedContainerColor = Color.Transparent,
                        unfocusedLeadingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedContainerColor = Color.DarkGray,
                        focusedTrailingIconColor = Color.White
                    )
                )

            }

            OutlinedTextField(value = reportDate,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Date") },
                onValueChange = {
                    reportDate = it
                },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    focusedTrailingIconColor = Color.White
                )
            )

            OutlinedTextField(value = reportTime,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Time") },
                onValueChange = {
                    reportTime = it
                },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    focusedTrailingIconColor = Color.White
                )
            )

            OutlinedTextField(value = reportDescription,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Description") },
                onValueChange = {
                    reportDescription = it
                },
                colors = TextFieldDefaults.colors(
                    unfocusedLabelColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedLeadingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedLabelColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.DarkGray,
                    focusedTrailingIconColor = Color.White
                )
            )

            // permission here...
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (cameraPermissionState.status.isGranted) {
                    Button(onClick = {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri) // opens the built in camera
                    }) {
                        Text(text = "Take photo")
                    }
                } else {
                    Column() {
                        val permissionText = if (cameraPermissionState.status.shouldShowRationale) {
                            "Please reconsider giving the camera persmission it is needed if you want to take photo for the message"
                        } else {
                            "Give permission for using photos with items"
                        }
                        Text(text = permissionText)
                        Button(onClick = {
                            cameraPermissionState.launchPermissionRequest()
                        }) {
                            Text(text = "Request permission")
                        }
                    }
                }

                Button(onClick = {
                    if (imageUri == null) {
                        reportIncidentViewModel.uploadIncident(reportType, reportLat, reportLong,
                            reportDate, reportTime, reportDescription)
                    } else {
                        reportIncidentViewModel.uploadIncidentWithImage(
                            context.contentResolver,
                            reportType, reportLat, reportLong, reportDate,
                            reportTime, reportDescription, imageUri!!
                        )
                    }
                }
                ) {
                    Text(text = "Upload")
                }


            }




            if (hasImage && imageUri != null) {
                AsyncImage(model = imageUri,
                    modifier = Modifier.size(200.dp, 200.dp),
                    contentDescription = "selected image")
            }

            when (reportIncidentViewModel.reportPageUiState) {
                is ReportPageUiState.LoadingReportUpload -> CircularProgressIndicator()
                is ReportPageUiState.ReportUploadSuccess -> {
                    Text(text = "Post uploaded.")
                    onReportIncidentSuccess()
                }
                is ReportPageUiState.ErrorDuringReportUpload ->
                    Text(text = "${(
                            reportIncidentViewModel.reportPageUiState as ReportPageUiState.ErrorDuringReportUpload).error}")

                is ReportPageUiState.LoadingImageUpload -> CircularProgressIndicator()
                is ReportPageUiState.ImageUploadSuccess -> {
                    Text(text = "Image uploaded, starting post upload.")
                }
                is ReportPageUiState.ErrorDuringImageUpload -> Text(text = "${(reportIncidentViewModel.reportPageUiState as ReportPageUiState.ErrorDuringImageUpload).error}")

                else -> {}
            }
        }

    }
}

class ComposeFileProvider : FileProvider(
    com.example.beware.R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPageTopBar(title: String) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        title = { Text(text = title, color = Color.White, fontSize = 30.sp) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.dark_grey)
        ),
        actions = {

        }
    )
}

