package com.example.jamapp.Model

import java.io.Serializable
import java.util.*

data class Event(
    val event_id : String = "",
    val host_id : String = "",
    val title : String = "",
    val address : String = "",
    val description : String = "",
    val imageLink : String = "",
    val learnMoreLink : String = "",
    val date : String = "",
    var attendanceCount : Int = 0
) : Serializable