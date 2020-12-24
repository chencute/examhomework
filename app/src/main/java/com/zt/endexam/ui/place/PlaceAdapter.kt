package com.zt.endexam.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.logic.model.Location

class PlaceAdapter (private val fragment:Fragment,private val locationList:List<Location>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        val placeName:TextView = view.findViewById(R.id.placeName)
        val placeAddress:TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = locationList[position]
        holder.placeName.text = location.name
        holder.placeAddress.text = location.country + " " + location.adm1 + " " + location.adm2
    }

}