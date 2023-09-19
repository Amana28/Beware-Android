package com.example.beware.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.beware.R
import com.example.beware.data.Report


@Composable
fun ProfileScreen(
    onCreateNewReportClick: ()->Unit = {},
    onBewareMapClick: ()->Unit = {},
    proileViewModel: ProfileViewModel = viewModel()
) {

    val reportListState = proileViewModel.reportsList().collectAsState(
        initial = ProfileUIState.Init)


    Scaffold(
        topBar = { ProfileTopBar(title = "Profile") },
        containerColor = colorResource(id = R.color.dark_grey),
        floatingActionButton = {
            ProfileFloatingActionButton(
                onCreateNewReportClick = onCreateNewReportClick
            )
        }
    ){contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {


            if (reportListState.value == ProfileUIState.Init) {
                Text(text = "Initializing..")
            } else if (reportListState.value is ProfileUIState.Success) {
                //Text(text = "Messages number: " +
                //        "${(postListState.value as MainScreenUIState.Success).postList.size}")
                LazyColumn() {
                    items((reportListState.value as ProfileUIState.Success).postList){
                        PostCard(report = it.report,
                            onRemoveItem = {
                                proileViewModel.deleteReport(it.reportId)
                            },
                            currentUserId = proileViewModel.currentUserId)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(title: String) {
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

@Composable
fun ProfileFloatingActionButton(
    onCreateNewReportClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            onCreateNewReportClick()
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
            modifier = Modifier.size(30.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    report: Report,
    onRemoveItem: () -> Unit = {},
    currentUserId: String = ""
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    LabelValueRow(label = "User Name", value = report.userName)
                    LabelValueRow(label = "Latitude", value = report.lat.toString())
                    LabelValueRow(label = "Longitude", value = report.long.toString())
                    LabelValueRow(label = "Date", value = report.date)
                    LabelValueRow(label = "Time", value = report.time)
                    LabelValueRow(label = "Description", value = report.description)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentUserId.equals(report.uid)) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.clickable {
                                onRemoveItem()
                            },
                            tint = Color.Red
                        )
                    }
                }
            }

            if (report.imgUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(report.imgUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}

@Composable
fun LabelValueRow(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}
