package com.example.jamapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamapp.Model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.view.*
import java.text.SimpleDateFormat
import java.util.*

class event_fragment : Fragment() {
    private lateinit var db : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Get chosen event from parent activity
        val thisActivity = activity as event_info
        var event_item = thisActivity.event

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        // Get live updates of the event
        db.child("event").child(event_item.event_id).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latestEvent = dataSnapshot.getValue(Event::class.java) as Event
                updateFragment(view, latestEvent)
            }
        })





        return view
    }

    public fun updateFragment(view : View, event_item : Event){
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
        view.event_count.setText(event_item.attendanceCount.toString() + " attending")
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

        // Hide certain buttons if this event is not hosted by current user
        if(event_item.host_id != auth.currentUser!!.uid) {
            view.edit_button.visibility = View.INVISIBLE
            view.edit_button.isClickable = false
            view.delete_button.visibility = View.INVISIBLE
            view.delete_button.isClickable = false
        } else {
            // Dont let the host register himself
            view.register_button.visibility = View.GONE
        }
    }


}
