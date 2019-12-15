package com.example.jamapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.jamapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*

/**
 * A simple [Fragment] subclass.
 */
class Register : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_register, container, false)

        // Set up the button
        view.backToLoginButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_register_to_login)
        }



        // Inflate the layout for this fragment
        return view
    }


}
