package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_event.*

class create_event : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = FirebaseDatabase.getInstance()
    private var dbRef = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize db
        auth = FirebaseAuth.getInstance()
    }

    public fun backToMyEvents(view : View){
        finish()
    }

    public fun createEvent(view : View){

        // Create event object
        var newEvent = Event( host_id = auth.uid.toString(), title = createEventTitle.text.toString(), address = createEventVenue.text.toString())
        dbRef.child("event").push().setValue(newEvent).addOnSuccessListener {
            // Show success message
            val toast = Toast.makeText(applicationContext, "Event created successfully", Toast.LENGTH_SHORT)
            Log.d("Event","Success")
            toast.show()
        }.addOnFailureListener {
            // Show success message
            val toast = Toast.makeText(applicationContext, "Could not create event", Toast.LENGTH_SHORT)
            Log.d("Event","FAILED")

            toast.show()
        }



        //redirect
        finish()
    }
}
