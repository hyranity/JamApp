package com.example.jamapp.Homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.Event
import com.example.jamapp.R
import kotlinx.android.synthetic.main.event_item.view.*

class HomeEventAdapter(val eventList : ArrayList<Event>) : RecyclerView.Adapter<HomeEventAdapter.ViewHolder>(){
    // Code possible thanks to : Simplified Coding @ https://www.youtube.com/watch?v=67hthq6Y2J8
    // Also thanks to : Kevin Moore @  https://www.raywenderlich.com/1560485-android-recyclerview-tutorial-with-kotlin#toc-anchor-006

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: HomeEventAdapter.ViewHolder, position: Int) {
        val event : Event = eventList[position]

        holder?.textName.text = event.title
        holder?.textAddress.text = event.address
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById(R.id.textName) as TextView
        val textAddress = itemView.findViewById(R.id.textAddress) as TextView
    }
}