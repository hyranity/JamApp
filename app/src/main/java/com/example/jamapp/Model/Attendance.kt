package com.example.jamapp.Model

import java.io.Serializable

data class Attendance (
    val event_id : String = "",
    val uid: String = ""
) : Serializable