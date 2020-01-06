package com.example.jamapp.Reports

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.jamapp.Model.Event
import com.example.jamapp.Model.Report
import com.example.jamapp.R

class ReportAdapter(val reportList: ArrayList<Report>) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.report_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun onBindViewHolder(holder: ReportAdapter.ViewHolder, position: Int) {

        val report: Report = reportList[position]
        holder?.textReportMsg.text = report.message
        holder?.textReportTitle.text = report.title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textReportTitle = itemView.findViewById(R.id.textReportTitle) as TextView
        val textReportMsg = itemView.findViewById(R.id.textReportMsg) as TextView
    }
}