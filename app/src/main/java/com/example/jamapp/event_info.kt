package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class event_info : AppCompatActivity() {
    // Get event object from passed inte nt
    public lateinit var event : Event

    private lateinit var db : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

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

    public fun isRegistered(id : String){
       // Check if user already registered to this event or not
    }

    // User joins event
    public fun joinEvent(view : View){
        // Add user to event as a registered user
        db.child("event").child(event.event_id).child("Attendees").setValue(auth.uid)
    }

}
