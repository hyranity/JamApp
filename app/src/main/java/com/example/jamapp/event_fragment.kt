package com.example.jamapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamapp.Model.Event
import com.example.jamapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*
import java.text.SimpleDateFormat
import java.util.*

class event_fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        // Get chosen event from parent activity
        val activity = activity as event_info
        val event_item = activity.event

        // Convert date to string
        val cal = Calendar.getInstance() as Calendar
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(event_item.date)

        val dateYear = cal.get(Calendar.YEAR)
        val dateMonth = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val dateDay = cal.get(Calendar.DAY_OF_MONTH)
        val dateStr =  dateDay.toString() + " " + dateMonth + " " + dateYear

        // Set the views
        view.event_date.text = dateStr
        view.event_title.text = event_item.title
        view.event_venue.text = event_item.address
        view.event_description.text = event_item.description
        view.event_learnMore.setOnClickListener{
            // Opens the link in default browser via implicit intent
            var link : String = event_item.learnMoreLink

            if(!link.startsWith("http://") && !link.startsWith("https://"))
                link = "http://" + link

            val learnMore = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(learnMore)
        }
        Picasso.get().setLoggingEnabled(true)
        Picasso.get().load(event_item.imageLink).into(view.imageView) // Set the image using Picasso library

        return view
    }

    // When I'm In button is pressed
    fun imIn() {
        // Get User object
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseDatabase.getInstance().reference

        // Get Event object
        val activity = activity as event_info
        var event = activity.event

        val ref = db.child("events").child(event.event_id).child("Attendees").child(user!!.uid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError : DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                // Check if User hasn't joined Event
                if (snapshot == null) {
                    // Add User ID to Attendees in Event object
                    db.child("events").child(event.event_id).child("Attendees").push().setValue(user!!.uid) // https://stackoverflow.com/a/40013420
                // Check if User has joined Event
                } else {
                    // Remove User ID from Attendees in Event object
                    snapshot.getRef().setValue(null) // https://stackoverflow.com/a/40070768
                }
            }
        })
    }

}
