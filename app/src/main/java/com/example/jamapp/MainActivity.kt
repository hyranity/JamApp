package com.example.jamapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.jamapp.ui.main.SectionsPagerAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = tabs

        tabs.setupWithViewPager(viewPager)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

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
    }

    private fun loadIcons(): Array<Int> {
        val icons = arrayOf(R.drawable.accountwhite_24dp, R.drawable.accountwhite_24dp, R.drawable.accountwhite_24dp)

        return icons
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

}