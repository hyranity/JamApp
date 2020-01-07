package com.example.jamapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_event.*
import java.text.SimpleDateFormat
import java.util.*

class create_event : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = FirebaseDatabase.getInstance()
    private var dbRef = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize db
        auth = FirebaseAuth.getInstance()

        // Create calendar to store event date
        val cal : Calendar = Calendar.getInstance()

        // Set date
        // Reference : https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/
        val datePicker = object : DatePickerDialog.OnDateSetListener{
            // Triggered when date is set
            override fun onDateSet(view : DatePicker, year : Int, month: Int, day: Int){

                // Set the selected date to calendar
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_YEAR, day)

                // Update date in EditText view
                val dateFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(dateFormat, Locale.UK)
                createEventDate!!.setText(sdf.format(cal.time))
            }
        }

        createEventDate.setOnClickListener{
            // Creates the date picker dialog when user clicks on the EditText field
            val datePickerDialog = DatePickerDialog(this, datePicker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    public fun backToMyEvents(view : View){
        finish()
    }

    public fun createEvent(view : View){
        // Convert the string to calendar
        val cal = Calendar.getInstance() as Calendar
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(createEventDate.text.toString())

        // create event id
        val event_id = dbRef.child("event").push().key as String

        // Create event object
        var newEvent = Event(event_id = event_id, host_id = auth.uid.toString(), title = createEventTitle.text.toString(), address = createEventVenue.text.toString(), description = createEventDescription.text.toString(), imageLink = createEventImageUrl.text.toString(), learnMoreLink = createEventWebLink.text.toString(), date = createEventDate.text.toString(), attendanceCount = 0)

        // Store new event under new event id
        dbRef.child("event").child(event_id).setValue(newEvent).addOnSuccessListener {
            // Show success message
            val toast =
                Toast.makeText(applicationContext, "Event created successfully", Toast.LENGTH_SHORT)
            Log.d("event_fragment","Success")
            toast.show()
        }.addOnFailureListener {
            // Show success message
            val toast = Toast.makeText(applicationContext, "Could not create event", Toast.LENGTH_SHORT)
            Log.d("event_fragment","FAILED")

            toast.show()
        }

        val user = auth!!.currentUser

        //Make the host participate (to show event under MyEvents)
        dbRef.child("users").child(user!!.uid).child("Participating").child(newEvent.event_id)
            .setValue(newEvent.event_id)

        // Add User ID to Attendees in Event object
        dbRef.child("event").child(newEvent.event_id).child("Attendees").child(user!!.uid)
            .setValue(user!!.uid) // https://stackoverflow.com/a/40013420

        //redirect
        finish()
    }
}
