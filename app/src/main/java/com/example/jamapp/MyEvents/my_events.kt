package com.example.jamapp.MyEvents


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jamapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

/**
 * A simple [Fragment] subclass.
 */
class my_events : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db : DatabaseReference

    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.fragment_my_events, container, false)

        // Inflate the layout for this fragment
        return view
    }

    fun createEvent(view : View) {
        // tbc
    }
}
