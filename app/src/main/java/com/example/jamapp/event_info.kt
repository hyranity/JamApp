package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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

    public fun isRegistered() : Boolean{
        var isRegistered = false

       // Check if user already registered to this event or not
        val ref = db.child("event").child(event.event_id).child("Attendees").child(auth.currentUser!!.uid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError : DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                Log.d("Attendees","Checking for existence")

                // Check if User hasn't joined Event
                if (!snapshot.exists()) {
                    Log.d("Attendees","User not registered")
                    isRegistered = false
                    // Check if User has joined Event
                } else {
                    Log.d("Attendees","User already registered")
                    isRegistered = true
                }
            }
        })

        return isRegistered
    }

    // When I'm In button is pressed
    public fun imIn(view : View) {
        val user = auth.currentUser

        val isRegistered = isRegistered()

        // If not registered, add user to event
        if(!isRegistered) {
            Log.d("Attendees","Registering user to event")
            // Add User ID to Attendees in Event object
            db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).setValue(user!!.uid) // https://stackoverflow.com/a/40013420
        }
        else{
            Log.d("Attendees","Removing user from event")
            // Remove User ID from Attendees in Event object
            db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).removeValue()// removes the value
        }
    }
}
