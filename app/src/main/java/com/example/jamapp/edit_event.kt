package com.example.jamapp

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_edit_event.*
import kotlinx.android.synthetic.main.fragment_edit_event.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [edit_event.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class edit_event : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_edit_event, container, false) as View

        // Get chosen event from parent activity
        val activity = activity as event_info
        var event = activity.event

        // Get live updates of the event so that the data is accurate
        db.child("event").child(event.event_id).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latestEvent = dataSnapshot.getValue(Event::class.java) as Event
                Log.d("EDIT EVENT", "Getting latest info")
                event = latestEvent
                // Put data into the fields
                view.editEventTitle.setText(latestEvent.title)
                view.editEventDate.setText(latestEvent.date)
                view.editEventVenue.setText(latestEvent.address)
                view.editEventDescription.setText(latestEvent.description)
                view.editEventImageUrl.setText(latestEvent.imageLink)
                view.editEventWebLink.setText(latestEvent.learnMoreLink)
            }
        })


        // For date setting
        // Create calendar to store event date
        val cal : Calendar = Calendar.getInstance()
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
                editEventDate!!.setText(sdf.format(cal.time))

            }
        }

        view.editEventDate.setOnClickListener{
            // Creates the date picker dialog when user clicks on the EditText field
            val datePickerDialog = DatePickerDialog(activity, datePicker, cal.get(Calendar.YEAR), cal.get(
                Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        return view
    }






}
