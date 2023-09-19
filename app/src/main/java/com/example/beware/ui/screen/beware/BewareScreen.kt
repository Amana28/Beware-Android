package com.example.beware.ui.screen.beware

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.round


@Composable
fun BewareScreen(
    bewareViewModel: BewareViewModel = viewModel(),
    onProfileIconClick: () -> Unit
) {

    var markerPosition = remember {
        listOf(LatLng(47.0, 19.0)).toMutableStateList()
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val reportListState = bewareViewModel.reportsList().collectAsState(
        initial = BewareUIState.Init)

    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                zoomGesturesEnabled = true
            )
        )
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isTrafficEnabled = true,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context,
                    R.raw.mapconfig)
            )
        )
    }


    if (reportListState.value == BewareUIState.Init) {
        Text(text = "Initializing..")
    }
    else if (reportListState.value is BewareUIState.Success) {
        LazyColumn() {
            items((reportListState.value as BewareUIState.Success).reportList) {
                markerPosition.add(LatLng(it.report.lat.toDouble(), it.report.long.toDouble()))
            }
        }
    }

    Scaffold(
        topBar = { BewareTopBar(title = "Beware Map", onProfileIconClick) },
        containerColor = colorResource(id = R.color.dark_grey)
    ){
        Column(modifier = Modifier.padding()) {

            val budapestLatLng = LatLng(47.4979, 19.0402)
            val zoomLevel = 15f
            var cameraState = rememberCameraPositionState {
                CameraPosition.fromLatLngZoom(budapestLatLng, zoomLevel)
            }
            var geocodeText by remember{ mutableStateOf("") }

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                cameraPositionState = cameraState,
                uiSettings = uiSettings,
                properties = mapProperties,
                onMapLoaded = {

                    val cameraPostion = CameraPosition.Builder()
                        .target(budapestLatLng)
                        .zoom(zoomLevel)
                        .build()
                    //cameraState.position = cameraPostion
                    coroutineScope.launch {
                        cameraState.animate(
                            CameraUpdateFactory.newCameraPosition(cameraPostion),3000
                        )
                    }

                }
            ){
                for (position in markerPosition) {
                    Marker(
                        state = MarkerState(position = position),
                        title = "My Marker",
                        snippet = "Marker description loc: ${position.latitude}, ${position.longitude}",
                        draggable = true,
                        onClick = {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                geocoder.getFromLocation(
                                    it.position.latitude,
                                    it.position.longitude,
                                    3,
                                    object : Geocoder.GeocodeListener {
                                        override fun onGeocode(addrs: MutableList<Address>) {
                                            val addr =
                                                "${addrs[0].getAddressLine(0)}, ${
                                                    addrs[0].getAddressLine(
                                                        1
                                                    )
                                                }, ${addrs[0].getAddressLine(2)}"

                                            geocodeText = addr
                                        }

                                        override fun onError(errorMessage: String?) {
                                            geocodeText = errorMessage!!
                                            super.onError(errorMessage)
                                        }
                                    })
                            }
                            true
                        }
                    )
                }

            }

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BewareTopBar(title: String, onProfileIconClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        title = { Text(text = title, color = Color.White, fontSize = 30.sp) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.dark_grey)
        ),
        actions = {
            IconButton(
                onClick = {
                    onProfileIconClick()
                }
            ) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Profile Icon",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    report: Report
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = report.description,
                    )
                }
            }
        }
    }
}


