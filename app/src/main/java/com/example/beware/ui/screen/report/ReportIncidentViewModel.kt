package com.example.beware.ui.screen.report

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beware.data.Report
import com.example.beware.data.ReportType
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*


sealed interface ReportPageUiState {
    object Init : ReportPageUiState

    object LoadingReportUpload : ReportPageUiState
    object ReportUploadSuccess : ReportPageUiState
    data class ErrorDuringReportUpload(val error: String?) : ReportPageUiState

    object LoadingImageUpload : ReportPageUiState
    object ImageUploadSuccess : ReportPageUiState
    data class ErrorDuringImageUpload(val error: String?) : ReportPageUiState
}

class ReportIncidentViewModel : ViewModel() {

    companion object {
        const val COLLECTION_REPORTS = "reports"
    }

    var reportPageUiState: ReportPageUiState by mutableStateOf(ReportPageUiState.Init)
    private lateinit var auth: FirebaseAuth

    init {
        auth = Firebase.auth
    }

    fun uploadIncident(type: String, lat: String, long: String, date: String, time: String, description: String, imgUrl: String = "") {
        reportPageUiState = ReportPageUiState.LoadingReportUpload

        val myReport = Report(
            uid = auth.currentUser!!.uid,
            userName = auth.currentUser!!.email!!,
            type = type,
            lat = lat,
            long = long,
            date = date,
            time = time,
            description = description,
            imgUrl = imgUrl
        )

        val reportsCollection = FirebaseFirestore.getInstance().collection(COLLECTION_REPORTS)

        reportsCollection.add(myReport).addOnSuccessListener {
            reportPageUiState = ReportPageUiState.ReportUploadSuccess
        }.addOnFailureListener{
            reportPageUiState = ReportPageUiState.ErrorDuringReportUpload(it.message)
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    public fun uploadIncidentWithImage(
        contentResolver: ContentResolver,
        type: String, lat: String, long: String, date: String, time: String, description: String,  imageUri: Uri
    ) {
        viewModelScope.launch {
            reportPageUiState = ReportPageUiState.LoadingImageUpload

            val source = ImageDecoder.createSource(contentResolver, imageUri)
            val bitmap = ImageDecoder.decodeBitmap(source)

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageInBytes = baos.toByteArray()

            // prepare the empty file in the cloud
            val storageRef = FirebaseStorage.getInstance().getReference()
            val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
            val newImagesRef = storageRef.child("images/$newImage")

            // upload the jpeg byte array to the created empty file
            newImagesRef.putBytes(imageInBytes)
                .addOnFailureListener { e ->
                    reportPageUiState = ReportPageUiState.ErrorDuringImageUpload(e.message)
                }.addOnSuccessListener { taskSnapshot ->
                    reportPageUiState = ReportPageUiState.ImageUploadSuccess

                    newImagesRef.downloadUrl.addOnCompleteListener(
                        object : OnCompleteListener<Uri> {
                            override fun onComplete(task: Task<Uri>) {
                                // the public URL of the image is: task.result.toString()
                                uploadIncident(type, lat, long, date, time, description, task.result.toString())
                            }
                        })
                }
        }
    }
}