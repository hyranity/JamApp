package com.example.jamapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail
import com.example.jamapp.Model.Event
import com.example.jamapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_announce.*
import kotlinx.android.synthetic.main.fragment_announce.view.*
import javax.mail.internet.MimeMessage

/**
 * A simple [Fragment] subclass.
 */
class announcement : Fragment() {
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var thisActivity: event_info
    private lateinit var event: Event

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val view = inflater.inflate(R.layout.fragment_announce, container, false)

        // Get activity
        thisActivity = activity as event_info
        // Get event
        event = thisActivity.event

        // Set display
        view.announce_event_title.text = event.title




        return view
    }


}
