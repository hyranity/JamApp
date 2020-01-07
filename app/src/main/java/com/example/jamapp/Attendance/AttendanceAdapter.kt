package com.example.jamapp.Attendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.User
import com.example.jamapp.R

class AttendanceAdapter(val attendanceList : ArrayList<User>) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttendanceAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.attendee, parent, false)
        return AttendanceAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }

    override fun onBindViewHolder(holder: AttendanceAdapter.ViewHolder, position: Int) {
        val user: User = attendanceList[position]
        holder?.attendeeName.text = user.name
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val attendeeName = itemView.findViewById<TextView>(R.id.textAttendeeName)
    }

}