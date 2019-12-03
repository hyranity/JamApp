package com.example.jamapp

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_landing.*

class landing : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        setSupportActionBar(toolbar)




    }

    // For login > register button
    public fun redirectRegister(view : View){
        view.findNavController().navigate(R.id.action_login_to_register)
    }

    // For register  > login button
    public fun redirectLogin(view : View){
        view.findNavController().navigate(R.id.action_register_to_login)
    }

}
