package com.zt.endexam.ui.place

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.zt.endexam.MainActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zt.endexam.R

class PlaceFragment : Fragment() {

    //创建viewModel
    val viewModel by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(PlaceViewModel::class.java)
    }
    //创建适配器
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
    }
}