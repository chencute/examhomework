package com.zt.endexam.ui.game

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R
import com.zt.endexam.logic.model.GameModel.CardMatchingGame

class CardAdapter(val game: CardMatchingGame): RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    //        获取布局内的对象
    inner class ViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.cardbutton)
    }

    //        回调函数
    var mListener: ((Int)->Unit)? = null
    fun setOnClickListener(l:(Int)->Unit) {
        mListener = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  game.cards.size
    }

    override fun onBindViewHolder(holder: CardAdapter.ViewHolder, position: Int) {
        val card = game.cardAtIndex(position)
        holder.button.isEnabled = !card.isMatched
        if (card.isChosen) {
            holder.button.text = card.toString()
            holder.button.setBackgroundColor(R.drawable.bg_card)
        } else {
            holder.button.text = ""
            holder.button.setBackgroundResource(R.drawable.bg_card_2)
        }
//    点击回调
        holder.button.setOnClickListener {
            mListener?.invoke(position)
        }
    }

}