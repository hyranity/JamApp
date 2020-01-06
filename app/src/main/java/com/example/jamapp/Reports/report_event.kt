package com.example.jamapp.Reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamapp.R
import com.example.jamapp.event_info
import kotlinx.android.synthetic.main.fragment_report_event.view.*

class report_event : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report_event, container, false)

        // Get chosen event from parent activity
        val activity = activity as event_info
        val event_item = activity.event

        view.report_event_title.text = event_item.title
        view.createReportTitle.setText("")

        return view
    }




}
