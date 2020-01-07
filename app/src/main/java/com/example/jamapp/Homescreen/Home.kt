package com.example.jamapp.Homescreen


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.MainActivity
import com.example.jamapp.Model.Event
import com.example.jamapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db : DatabaseReference

    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("event")

        // Some code from https://stackoverflow.com/questions/30093111/findviewbyid-not-working-in-fragment
        // Also with code from https://www.youtube.com/watch?v=67hthq6Y2J8

        val rootView =  inflater.inflate(R.layout.fragment_home, container, false) as View

        val recyclerView = rootView.findViewById(R.id.Recycler) as RecyclerView

        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = linearLayoutManager

        val events = arrayListOf<Event>()
        val adapter = HomeEventAdapter(events, context!!)
        recyclerView.adapter = adapter

        // GET EVENTS
        Log.d("EVENTLIST", "Loading events")
        db.orderByChild("attendanceCount").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }

            // When events are added/deleted
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the list first
                events.clear()

                // Get event data
                for (item in dataSnapshot.children) {
                    val event = item.getValue(Event::class.java) as Event
                    events.add(event)
                }

                // Reverse order from highest attendance to lowest
                events.reverse()
                // Update adapter
                adapter.notifyDataSetChanged()
            }
        })



        // Inflate the layout for this fragment
        return rootView
    }


}
