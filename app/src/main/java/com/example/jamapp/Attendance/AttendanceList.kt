package com.example.jamapp.Attendance


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.User
import com.example.jamapp.R
import com.example.jamapp.event_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_attendance.view.*

/**
 * A simple [Fragment] subclass.
 */
class AttendanceList : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_attendance, container, false) as View

        // get activity and event
        val activity = activity as event_info
        val event = activity.event

        view.attendanceEventTitle.text = event.title


        var attendeesList = ArrayList<User>()

        var recyclerView = view.findViewById<RecyclerView>(R.id.AttendanceRecycler)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = linearLayoutManager

        val adapter = AttendanceAdapter(attendeesList)
        recyclerView.adapter = adapter

        // Get IDs of attendees
        db.child("event").child(event.event_id).child("Attendees")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Can't obtain attendees", databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    attendeesList.clear()
                    for (item in dataSnapshot.children) {
                        // For each ID
                        val userId = item.getValue(String::class.java) as String
                        // Get user object
                        db.child("users").child(userId)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e("Can't obtain attendees", databaseError.message)
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Add user object to attendance list
                                    val user = dataSnapshot.getValue(User::class.java) as User

                                    attendeesList.add(user)
                                    adapter.notifyDataSetChanged()
                                }
                            })
                    }

                    adapter.notifyDataSetChanged()
                }

            })

        return view
    }


}
