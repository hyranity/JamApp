package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.jamapp.Model.Event
import com.example.jamapp.Model.Report
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_report_event.*

class event_info : AppCompatActivity() {
    // Get event object from passed intent
    public lateinit var event : Event

    private lateinit var db : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Get extra
        event =  intent.getSerializableExtra("event") as Event

        // Check if user already registered to this event or not
        val ref = db.child("users").child(auth.currentUser!!.uid).child("Participating")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError : DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                Log.d("Attendees","Checking for existence")

                // Check if User hasn't joined Event
                if (!snapshot.hasChild(event.event_id)) {
                    Log.d("Attendees","User not registered")
                    Log.d("Attendees","Registering user to event")
                    isRegistered = false;
                    register_button.setText("I'M IN!")

                    // Check if User has joined Event
                } else {
                    Log.d("Attendees","User already registered")
                    Log.d("Attendees","Removing user from event")
                    isRegistered = true;
                    register_button.setText("UNATTEND!")
                }
            }
        })


        setContentView(R.layout.activity_event_info)

    }

    // Go back to event fragment
    public fun backToEventInfo(view : View){
        view.findNavController().navigate(R.id.action_report_event_to_event)
    }

    // Go to report event screen
    public fun reportEvent(view : View){
        view.findNavController().navigate(R.id.action_event_to_report_event)
    }

    // Back to home
    public fun backToHome(view : View){
        finish()
    }

    // When I'm In button is pressed
    public fun imIn(view : View) {
       if(isRegistered){
           unregisterUser()
           isRegistered = false
           register_button.setText("I'M IN!")

           // Decrease count in db
           event.attendanceCount = event.attendanceCount-1
           db.child("event").child(event.event_id).setValue(event)
           db.child("event").child(event.event_id).child("attendanceCount").setValue(event.attendanceCount)

           // Show success message
           val toast = Toast.makeText(applicationContext, "You are no longer registered to this event,", Toast.LENGTH_SHORT)
           toast.show()
           }
        else {
           registerUser()
           isRegistered = true
           register_button.setText("UNATTEND!")

           // Increase count in db
           event.attendanceCount = event.attendanceCount+1
           db.child("event").child(event.event_id).setValue(event)
           db.child("event").child(event.event_id).child("attendanceCount").setValue(event.attendanceCount)

           val toast = Toast.makeText(applicationContext, "You are now registered to this event,", Toast.LENGTH_SHORT)
           toast.show()
       }

    }

    public fun registerUser(){
        val user = auth.currentUser
        // Add User ID to Attendees in Event object
        db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).setValue(user!!.uid) // https://stackoverflow.com/a/40013420

        //Add Event ID to Participations in User object
        db.child("users").child(user!!.uid).child("Participating").child(event.event_id).setValue(event.event_id)
    }

    public fun unregisterUser(){
        val user = auth.currentUser
        // Remove User ID from Attendees in Event object
        db.child("event").child(event.event_id).child("Attendees").child(user!!.uid).removeValue()// removes the value

        //Remove Event ID from Participations in User object
        db.child("users").child(user!!.uid).child("Participating").child(event.event_id).removeValue()
    }

    // Submit a report about the event
    public fun submitReport(view : View){
        val reasonText = findViewById(R.id.reason) as EditText
        // Obtain report details
        val report = Report(reporter_id = auth.currentUser!!.uid, message = reasonText.text.toString())

        // Push to database
        db.child("event").child(event.event_id).child("reports").push().setValue(report)


        // Redirect
        backToEventInfo(view)
    }
}
