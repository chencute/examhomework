package com.zt.endexam.ui.music

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.SunnyWeatherApplication
import com.zt.endexam.logic.Repository
import com.zt.endexam.logic.model.MusicData


class MusicAdapter(private val fragment: musicListFragment,private val musicData:List<MusicData>) : RecyclerView.Adapter<MusicAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val musicName: TextView = view.findViewById(R.id.musicName)
        val artistName: TextView = view.findViewById(R.id.artistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return musicData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicData[position]
        holder.musicName.text = music.musicName
        holder.artistName.text = music.artistName
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            Log.d("Test","position=${position}")
            Repository.savecurrent(position)
//           pushFragment(fragment,SunnyWeatherApplication.context)
            (SunnyWeatherApplication.context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_box, fragment)
                .commit()
        }
    }

    fun pushFragment(
        newFragment: Fragment?,
        context: Context
    ) {
            Log.d("Test","${newFragment}")
        val transaction: FragmentTransaction =
            (context as FragmentActivity).supportFragmentManager.beginTransaction()
        newFragment?.let { transaction.replace(R.id.frameLayout_box, it) }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}