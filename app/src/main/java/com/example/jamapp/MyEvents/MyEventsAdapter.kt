package com.example.jamapp.MyEvents

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Homescreen.HomeEventAdapter
import com.example.jamapp.Model.Event
import com.example.jamapp.MyEvents.my_events
import com.example.jamapp.R
import com.example.jamapp.event_info

class MyEventsAdapter(val eventList : ArrayList<Event>, val context : Context) : RecyclerView.Adapter<MyEventsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event : Event = eventList[position]

        holder?.textName.text = event.title
        holder?.textAddress.text = event.address
        holder.event_card.setOnClickListener{

            val intent = Intent(context, event_info::class.java)
            intent.putExtra("event", event) // Pass the event object
            context.startActivity(intent) // Start the event_fragment Info activity
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById(R.id.textName) as TextView
        val textAddress = itemView.findViewById(R.id.textAddress) as TextView
        val event_card = itemView.findViewById(R.id.event_card) as CardView
    }

}