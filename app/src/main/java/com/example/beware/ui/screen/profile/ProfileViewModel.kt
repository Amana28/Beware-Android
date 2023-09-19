package com.example.beware.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.example.beware.data.Report
import com.example.beware.data.ReportWithId
import com.example.beware.ui.screen.report.ReportIncidentViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed interface ProfileUIState {
    object Init : ProfileUIState
    data class Success(val postList: List<ReportWithId>) : ProfileUIState
    data class Error(val error: String?) : ProfileUIState
}

class ProfileViewModel() : ViewModel(){

    var currentUserId: String

    init {
        currentUserId = Firebase.auth.currentUser!!.uid
    }

    fun reportsList() = callbackFlow {
        val snapshotListener =
            FirebaseFirestore.getInstance().collection(ReportIncidentViewModel.COLLECTION_REPORTS)
                .addSnapshotListener() { snapshot, e ->
                    val response = if (snapshot != null) {
                        val postList = snapshot.toObjects(Report::class.java)
                        val postWithIdList = mutableListOf<ReportWithId>()

                        postList.forEachIndexed { index, post ->
                            postWithIdList.add(ReportWithId(snapshot.documents[index].id, post))
                        }

                        ProfileUIState.Success(
                            postWithIdList
                        )
                    } else {
                        ProfileUIState.Error(e?.message.toString())
                    }

                    trySend(response) // emit this value through the flow
                }
        awaitClose {
            snapshotListener.remove()
        }
    }


    fun deleteReport(postKey: String) {
        FirebaseFirestore.getInstance().collection(
            ReportIncidentViewModel.COLLECTION_REPORTS
        ).document(postKey).delete()
    }


}