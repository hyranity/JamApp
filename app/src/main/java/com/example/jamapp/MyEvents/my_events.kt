package com.example.jamapp.MyEvents


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Homescreen.HomeEventAdapter
import com.example.jamapp.Model.Event
import com.example.jamapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class my_events : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Get the list of events the user is attending
        val eventRef = db.child("users").child(auth.currentUser!!.uid).child("Participating")

        val rootView = inflater.inflate(R.layout.fragment_my_events, container, false) as View

        val recyclerView = rootView.findViewById(R.id.MyEventsRecycler) as RecyclerView

        linearLayoutManager = LinearLayoutManager(context)

        recyclerView!!.layoutManager = linearLayoutManager

        val events = arrayListOf<Event>()
        val adapter = MyEventsAdapter(events, context!!)
        recyclerView.adapter = adapter

        val eventIDs = arrayListOf<String>()

        db.child("users").child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }

                override fun onDataChange(userSnapshot: DataSnapshot) {
                    Log.e("CHECKING", "User details changed")
                    // For each participating event
                    eventRef.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException()
                        }


                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            events.clear()
                            Log.e("CHECKING", "Updating myEvents")
                            // Get event ID
                            for (item in dataSnapshot.children) {
                                val eventId = item.getValue(String::class.java) as String


                                // Get individual event details
                                db.child("event").child(eventId)
                                    .addValueEventListener(object : ValueEventListener {
                                        override fun onCancelled(databaseError: DatabaseError) {
                                            throw databaseError.toException()
                                        }


                                        override fun onDataChange(dataSnapshotTwo: DataSnapshot) {
                                            // Clear the list first


                                            val event =
                                                dataSnapshotTwo.getValue(Event::class.java) as Event
                                            events.add(event)


                                            // Update adapter
                                            adapter.notifyDataSetChanged()
                                        }
                                    })


                            }
                            // Update adapter
                            adapter.notifyDataSetChanged()
                        }

                    })
                    // Update adapter
                    adapter.notifyDataSetChanged()
                }


            })


        // Inflate the layout for this fragment
        return rootView
    }

    fun isRegistered(event: Event): Boolean {
        var isRegistered = false
        val ref =
            db.child("event").child(event.event_id).child("Attendees").child(auth.currentUser!!.uid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Attendees", "Checking for existence")

                // Check if User hasn't joined Event
                if (!snapshot.exists()) {
                    Log.d("Attendees", "User not registered")
                    isRegistered = false
                    // Check if User has joined Event
                } else {
                    Log.d("Attendees", "User already registered")
                    isRegistered = true
                }
            }
        })

        return isRegistered
    }

    fun createEvent(view: View) {
        // tbc
    }
}
