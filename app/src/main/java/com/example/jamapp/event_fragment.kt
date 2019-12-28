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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_event.view.*

class event_fragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_event, container, false)

        // Get chosen event from parent activity
        val activity = activity as event_info
        val event_item = activity.event as Event

        // Set the views
        view.event_title.text = event_item.title
        view.event_venue.text = event_item.address
        view.event_description.text = event_item.description
        view.event_learnMore.setOnClickListener{
            // Opens the link in default browser via implicit intent
            val learnMore = Intent(Intent.ACTION_VIEW)
            learnMore.data = Uri.parse(event_item.learnMoreLink)
            startActivity(learnMore)
        }
        Picasso.get().load(event_item.imageLink).into(view.imageView) // Set the image using Picasso library

        return view
    }

}
