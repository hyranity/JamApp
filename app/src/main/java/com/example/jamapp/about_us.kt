package com.example.jamapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class about_us : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
    }

    // Close this activity
    public fun closeAbout(view: View) {
        finish()
    }
}
