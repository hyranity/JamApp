package com.example.jamapp.Homescreen


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.Event
import com.example.jamapp.R



/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {

    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Some code from https://stackoverflow.com/questions/30093111/findviewbyid-not-working-in-fragment
        // Also with code from https://www.youtube.com/watch?v=67hthq6Y2J8

        val rootView =  inflater.inflate(R.layout.fragment_home, container, false) as View

        val recyclerView = rootView.findViewById(R.id.Recycler) as RecyclerView

        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = linearLayoutManager

        val eventList = ArrayList<Event>()

        // Dummy events data
        eventList.add(Event("TARUC Graduation Ceremony", "Dewan Utama, TARUC Main Campus"))
        eventList.add(Event("Anime Fiesta 2019", "Kuala Lumpur Convention Center (Hall 3)"))
        eventList.add(Event("Choral Exchange 2019", "Dewan Utama, TARUC Main Campus"))
        eventList.add(Event("Johann's Birthday", "BestHouseEver"))
        val adapter = HomeEventAdapter(eventList)

        recyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return rootView
    }


}
