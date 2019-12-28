package com.example.jamapp.Model

import java.io.Serializable

data class Event(val host_id : String = "", val title : String = "", val address : String = "", val description : String = "", val imageLink : String = "", val learnMoreLink : String = "") : Serializable