package com.example.jamapp.Reports


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.Report
import com.example.jamapp.R
import com.example.jamapp.event_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_view_reports.view.*

/**
 * A simple [Fragment] subclass.
 */
class ViewReports : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

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
        val view = inflater.inflate(R.layout.fragment_view_reports, container, false)
        view.report_event_title.text = event_item.title
        // Get recycler
        val recyclerView = view.findViewById<RecyclerView>(R.id.ReportRecycler)

        // Create array
        val reportsList = ArrayList<Report>()

        val linearLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = linearLayoutManager

// Pass to adapter to inflate recyclerview
        val adapter = ReportAdapter(reportsList)

        // Get reports from DB
        db.child("event").child(event_item.event_id).child("reports")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Cannot obtain reports", databaseError.message)
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    reportsList.clear()
                    for (reportItem in dataSnapshot.children) {
                        val report = reportItem.getValue(Report::class.java) as Report
                        reportsList.add(report)

                        Log.e("TEST", report.message)
                    }

                    adapter.notifyDataSetChanged()
                }
            })


        recyclerView.adapter = adapter

        return view
    }


}
