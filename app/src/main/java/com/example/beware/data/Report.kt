package com.example.beware.data

data class Report(
    var uid: String = "",
    var userName: String = "",
    var type : String = "",
    var lat: String = "",
    var long: String = "",
    var date: String = "",
    var time: String = "",
    var description: String = "",
    var imgUrl: String = ""
)

data class ReportWithId(
    val reportId: String,
    val report: Report
)

enum class ReportType{
    CAR_ACCIDENT, ROBBERY, MISSING_ITEM;
}

