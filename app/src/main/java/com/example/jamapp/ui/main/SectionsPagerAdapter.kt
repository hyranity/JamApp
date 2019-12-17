package com.example.jamapp.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.jamapp.Homescreen.Home
import com.example.jamapp.MyAccount
import com.example.jamapp.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)


// Code is possible thanks to Eijaz Allibhai @ https://medium.com/@eijaz/getting-started-with-tablayout-in-android-kotlin-bb7e21783761
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val pos : Int = position + 1
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when(pos){
            2 -> {
                Home()
            }
            1 -> {
                MyAccount()
            }
            else -> {
                return PlaceholderFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
       //return context.resources.getString(TAB_TITLES[position])

        // Null means no text is returned. Reference : https://stackoverflow.com/questions/30892545/tablayout-with-icons-only/37560365
        return null
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}