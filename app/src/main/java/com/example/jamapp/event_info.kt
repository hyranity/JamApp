package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jamapp.Model.Event

class event_info : AppCompatActivity() {
    // Get event object from passed inte nt
    public lateinit var event : Event

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get extra
        event =  intent.getSerializableExtra("event") as Event

        // Debug purposes
        Log.d("Test", event.title)

        setContentView(R.layout.activity_event_info)

    }


    // Back to home
    public fun backToHome(view : View){
        finish()
    }

}
