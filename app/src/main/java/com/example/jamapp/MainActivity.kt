package com.example.jamapp

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.jamapp.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

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

}