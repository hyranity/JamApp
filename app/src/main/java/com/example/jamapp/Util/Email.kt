package com.example.jamapp.Util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail

public fun sendEmail(
    context: Context,
    emailList: ArrayList<String>,
    title: String,
    message: String,
    subject: String
) {

    val email = BackgroundMail.newBuilder(context)
        .withUsername("th3jamapp@gmail.com")
        .withPassword("jamapp123")
        .withType(BackgroundMail.TYPE_HTML)
        .withBody("<h1>" + title + "</h1><br/>" + message)
        .withSubject(subject)
        .withOnSuccessCallback {
            Log.d("Email", "success")
        }
        .withOnFailCallback {
            Log.d("Email", "failed")
        }


    // Send to all receipients
    for (receipientEmail in emailList) {
        email.withMailto(receipientEmail)
        Log.d("email", receipientEmail)
    }

    // Send email
    if (emailList.size > 0)
        email.send()

}

public fun sendSingleEmail(
    context: Context,
    targetEmail: String,
    title: String,
    message: String,
    subject: String
) {

    val email = BackgroundMail.newBuilder(context)
        .withUsername("th3jamapp@gmail.com")
        .withPassword("jamapp123")
        .withType(BackgroundMail.TYPE_HTML)
        .withBody("<h1>" + title + "</h1><br/>" + message)
        .withSubject(subject)
        .withOnSuccessCallback {
            Log.d("Email", "success")
        }
        .withOnFailCallback {
            Log.d("Email", "failed")
        }
    email.withMailto(targetEmail)

    email.send()

}
