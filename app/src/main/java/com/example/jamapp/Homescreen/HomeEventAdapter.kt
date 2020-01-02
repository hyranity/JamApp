package com.example.jamapp.Homescreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.MainActivity
import com.example.jamapp.Model.Event
import com.example.jamapp.R
import com.example.jamapp.event_info
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_item.view.*

class HomeEventAdapter(val eventList : ArrayList<Event>,  val context : Context) : RecyclerView.Adapter<HomeEventAdapter.ViewHolder>(){
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
        Picasso.get().setLoggingEnabled(true)
        Picasso.get().load(event.imageLink).into(holder?.image) // Set the image using Picasso library
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
        val image = itemView.findViewById(R.id.homeEventImage) as ImageView
    }


}