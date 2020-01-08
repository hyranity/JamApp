package com.example.jamapp.MyEvents


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Homescreen.HomeEventAdapter
import com.example.jamapp.MainActivity
import com.example.jamapp.Model.Event
import com.example.jamapp.R
import com.example.jamapp.event_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_my_events.view.*
import java.text.SimpleDateFormat
import java.util.*

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
        // Get activity
        val activity = activity as MainActivity

        val rootView = inflater.inflate(R.layout.fragment_my_events, container, false) as View

        loadMyEvents(rootView)

        rootView.refreshBt.setOnClickListener {
            loadMyEvents(rootView)
            Toast.makeText(activity, "Refreshed your events", Toast.LENGTH_SHORT).show()
        }


        // Inflate the layout for this fragment
        return rootView
    }

    public fun loadMyEvents(rootView: View) {
        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        // Get the list of events the user is attending
        val eventRef = db.child("users").child(auth.currentUser!!.uid).child("Participating")

        val recyclerView = rootView.findViewById(R.id.MyEventsRecycler) as RecyclerView

        linearLayoutManager = LinearLayoutManager(context)

        recyclerView!!.layoutManager = linearLayoutManager

        val events = arrayListOf<Event>()
        val adapter = MyEventsAdapter(events, context!!)
        recyclerView.adapter = adapter

        val eventIDs = arrayListOf<String>()

        db.child("users").child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }

                override fun onDataChange(userSnapshot: DataSnapshot) {

                    // For each participating event
                    eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            throw databaseError.toException()
                        }


                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            events.clear()

                            // Get event ID
                            for (item in dataSnapshot.children) {
                                val eventId = item.getValue(String::class.java) as String

                                // Get individual event details
                                db.child("event").child(eventId)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onCancelled(databaseError: DatabaseError) {
                                            throw databaseError.toException()
                                        }


                                        override fun onDataChange(dataSnapshotTwo: DataSnapshot) {
                                            if (dataSnapshotTwo.exists()) {
                                                val event =
                                                    dataSnapshotTwo.getValue(Event::class.java) as Event


                                                events.add(event)

                                            } else {
                                                // if the event that this user is participating does not exist, delete from the user's PARTICIPATING list
                                                db.child("users").child(auth.currentUser!!.uid)
                                                    .child("Participating").child(eventId)
                                                    .removeValue()
                                            }

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
