package com.example.jamapp

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_event, container, false) as View

        // Get chosen event from parent activity
        val activity = activity as event_info
        var event = activity.event

        // Put data into the fields
        view.editEventTitle.setText(event.title)
        view.editEventDate.setText(event.date)
        view.editEventVenue.setText(event.address)
        view.editEventDescription.setText(event.description)
        view.editEventImageUrl.setText(event.imageLink)
        view.editEventWebLink.setText(event.learnMoreLink)

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
                view.editEventDate!!.setText(sdf.format(cal.time))
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
