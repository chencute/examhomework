package com.zt.endexam.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.logic.model.GameModel.Score

class dialogAdapter(val scoreList:List<Score>): RecyclerView.Adapter<dialogAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val scoredTime = view.findViewById<TextView>(R.id.scoredTime)
        val scoredGame = view.findViewById<TextView>(R.id.scoredGame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): dialogAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    override fun onBindViewHolder(holder: dialogAdapter.ViewHolder, position: Int) {
        val score = scoreList[position]
        holder.scoredTime.text = score.date
        holder.scoredGame.text = score.score.toString()
    }
}