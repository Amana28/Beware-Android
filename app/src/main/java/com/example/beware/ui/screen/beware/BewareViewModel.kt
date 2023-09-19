package com.example.beware.ui.screen.beware

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beware.data.Report
import com.example.beware.data.ReportWithId
import com.example.beware.ui.screen.report.ReportIncidentViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow


sealed interface BewareUIState {
    object Init : BewareUIState

    data class Success(val reportList: List<ReportWithId>) : BewareUIState
    data class Error(val error: String?) : BewareUIState
}

class BewareViewModel() : ViewModel() {

    var currentUserId: String

    init {
        //auth = FirebaseAuth.getInstance()
        currentUserId = Firebase.auth.currentUser!!.uid
        //currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun reportsList() = callbackFlow {
        val snapshotListener =
            FirebaseFirestore.getInstance().collection(ReportIncidentViewModel.COLLECTION_REPORTS)
                .addSnapshotListener() { snapshot, e ->
                    val response = if (snapshot != null) {
                        val reportList = snapshot.toObjects(Report::class.java)
                        val reportWithIdList = mutableListOf<ReportWithId>()

                        reportList.forEachIndexed { index, report ->
                            reportWithIdList.add(ReportWithId(snapshot.documents[index].id, report))
                        }

                        BewareUIState.Success(
                            reportWithIdList
                        )
                    } else {
                        BewareUIState.Error(e?.message.toString())
                    }

                    trySend(response) // emit this value through the flow
                }
        awaitClose {
            snapshotListener.remove()
        }
    }

}