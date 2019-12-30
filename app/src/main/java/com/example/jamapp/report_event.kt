package com.example.jamapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.jamapp.Model.Event
import kotlinx.android.synthetic.main.fragment_report_event.*
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
        view.reason.setText("")

        return view
    }




}
