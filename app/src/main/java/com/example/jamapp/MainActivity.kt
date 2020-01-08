package com.example.jamapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.jamapp.ui.main.SectionsPagerAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_account.*

class MainActivity : AppCompatActivity() {

    private lateinit var db : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = tabs

        tabs.setupWithViewPager(viewPager)

        //Set icons to the tab
        // Reference: https://medium.com/@droidbyme/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff
        val icons = loadIcons()
        tabs.getTabAt(0)?.setIcon(icons[0])
        tabs.getTabAt(1)?.setIcon(icons[1])
        tabs.getTabAt(2)?.setIcon(icons[2])


        tabs.setSelectedTabIndicatorColor(Color.WHITE)
        tabs.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_STRETCH)

        // Test link
        val textView : TextView = findViewById(R.id.textView)
        textView.movementMethod = LinkMovementMethod.getInstance()

        // To change icon colors
        /*
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
               return

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                tabs.getTabAt(tabs.selectedTabPosition)?.setIcon(R.drawable.ic_account) // Set to white icon

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                tabs.getTabAt(tabs.selectedTabPosition)?.setIcon(R.drawable.ic_launcher_background) // Set to white icon


            }

        })

         */
        // Start at the middle tab
        viewPager.setCurrentItem(1)
    }

    private fun loadIcons(): Array<Int> {
        val icons = arrayOf(
            R.drawable.accountwhite_24dp,
            R.drawable.ic_event_black_24dp,
            R.drawable.ic_event_note_black_24dp
        )

        return icons
    }

    public fun createEvent(view : View){
        val intent = Intent(this, create_event::class.java)
        startActivity(intent)
    }

    public fun performLogout(view : View){
      val  auth = FirebaseAuth.getInstance();
        auth.signOut()

        // redirect back to login screen
        val intent = Intent(this, LandingActivity::class.java)
        val toast = Toast.makeText(applicationContext, "Logged out successfully", Toast.LENGTH_SHORT)
        toast.show()
        startActivity(intent)
        finish()
    }

    public fun updateAccount(view : View){
        if(editAccountEmail.text.isBlank() || editAccountName.text.isBlank()){
            // empty fields
            val toast = Toast.makeText(applicationContext, "Ensure both email & name is filled", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        if(confirmEditPassword.text.isBlank()){
            // empty fields
            val toast = Toast.makeText(applicationContext, "Enter current password to confirm changes", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        var updatePassword = false
        // Perform checks
        if(!editAccounPassword.text.isBlank())
            updatePassword = true

        if(updatePassword && editAccounPassword.text.toString().length < 6){
            // Invalid password length
            val toast = Toast.makeText(applicationContext, "Password must be 6 characters long", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        // VALIDATION : ensure email input is valid - https://stackoverflow.com/a/7882950
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editAccountEmail.text.toString()).matches()) {
            val toast = Toast.makeText(applicationContext, "Your email should use the format eg. example@mail.com.", Toast.LENGTH_SHORT)
            toast.show()
            return
        }

        //Re authenticate user
        val credential = EmailAuthProvider.getCredential(auth.currentUser?.email.toString(), confirmEditPassword.text.toString())
        Log.d("AUTHENTICATE", confirmEditPassword.text.toString())


        auth.currentUser?.reauthenticate(credential)?.addOnSuccessListener{
           Log.d("My Account", "User re-authenticated")
            // Perform update
            auth.currentUser?.updateEmail(editAccountEmail.text.toString())?.addOnCompleteListener{Log.d("MyAccount","Email updated")}

            if(updatePassword)
                auth.currentUser?.updatePassword(editAccounPassword.text.toString())?.addOnCompleteListener{Log.d("MyAccount","Password updated")}

            db.child("users").child(auth.currentUser!!.uid).child("name").setValue(editAccountName.text.toString())
            db.child("users").child(auth.currentUser!!.uid).child("email").setValue(editAccountEmail.text.toString())

            val toast = Toast.makeText(applicationContext, "Account changes saved", Toast.LENGTH_SHORT)
            toast.show()
       }?.addOnFailureListener {
            Log.w("MyAccount","Wrong password")
            val toast = Toast.makeText(applicationContext, "Your current password is wrong", Toast.LENGTH_SHORT)
            toast.show()
        }


    }

}