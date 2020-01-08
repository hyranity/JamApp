package com.example.jamapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jamapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyAcvcount.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyAcvcount.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAccount : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db : DatabaseReference
    private var user = User("", "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View =  inflater!!.inflate(R.layout.fragment_my_account, container, false)

        // Initialize db
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val ref = db.child("users").child(auth.currentUser!!.uid)
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError : DatabaseError) {
                Log.w("Error", databaseError.toString())
            }

            override fun onDataChange(snapshot : DataSnapshot) {
                // If doesn't exists
                if (!snapshot.exists()) {
                    // Logout the user
                    val activity = activity as MainActivity
                    activity.performLogout(view)
                }
                // Get user data as object
                var userData = snapshot.getValue(User::class.java)
                user = userData!!
                view.editAccountName.setText(user.name)
                view.editAccountEmail.setText(user.email)
            }
        })



        // Inflate the layout for this fragment
        return view
    }



}


