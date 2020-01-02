package com.example.jamapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.jamapp.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class LandingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        setSupportActionBar(toolbar)

        //initialize
        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()


    }

    public override fun onStart(){
        super.onStart()
        updateUI()
    }

    private fun updateUI(){
        // Check if user already sign-in
        val currentUser = auth.currentUser
        if(currentUser != null)
            loginUser()
    }

    private fun loginUser(){
        val intent = Intent(this, MainActivity::class.java)
        val toast = Toast.makeText(applicationContext, "Login success!", Toast.LENGTH_SHORT)
        toast.show()
        startActivity(intent)
        finish()
    }

    // For login > register button
    public fun redirectRegister(view : View){
        view.findNavController().navigate(R.id.action_login_to_register)
    }

    // When user tap on Register
    public fun performRegister(view : View){
        if(registerEmail.text.isBlank() || passwordRegister.text.isBlank() || confirmPasswordRegister.text.isBlank() || registerName.text.isBlank()){
            val toast = Toast.makeText(applicationContext, "Ensure all fields are filled in", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if(passwordRegister.text.toString() != confirmPasswordRegister.text.toString()){
            val toast = Toast.makeText(applicationContext, "Your passwords do not match.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        auth.createUserWithEmailAndPassword(registerEmail.text.toString(), passwordRegister.text.toString())
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    // Successful registration
                    Log.d("REGISTER","Registration success")
                    val toast = Toast.makeText(applicationContext, "Account successfully created!", Toast.LENGTH_SHORT)
                    toast.show()
                    val user = auth.currentUser

                    // Create user object
                    var newUser = User(uid = user!!.uid, email = registerEmail.text.toString(), name = registerName.text.toString())

                    // Store in firebase realtime db
                    //val key = db.child("users").push().key
                   db.child("users").child(user!!.uid).setValue(newUser)
                    loginUser()
                } else{
                    Log.w("REGISTER FAILED", "Could not register user", task.exception)
                    val toast = Toast.makeText(applicationContext, "Could not register account", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
    }

    public fun performLogin(view : View){

        if(emailLogin.text.isBlank() || passwordLogin.text.isBlank()){
            val toast = Toast.makeText(applicationContext, "Ensure email & password fields are filled in", Toast.LENGTH_SHORT)
            toast.show()
            return
        }
        else{
        auth.signInWithEmailAndPassword(emailLogin.text.toString(), passwordLogin.text.toString())
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    // Successful registration
                    Log.d("LOGIN","LOGIN SUCCESS")
                    val user = auth.currentUser
                    updateUI()
                } else{
                    Log.w("LOGIN", "Could not login user", task.exception)
                    val toast = Toast.makeText(applicationContext, "Invalid email or password", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }


    }

    // For register  > login button
    public fun redirectLogin(view : View){
        view.findNavController().navigate(R.id.action_register_to_login)
    }

}
