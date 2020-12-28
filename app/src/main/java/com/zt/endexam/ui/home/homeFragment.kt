package com.zt.endexam.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zt.endexam.R
import com.zt.endexam.ui.place.PlaceViewModel


class homeFragment : Fragment() {

    companion object {
        fun newInstance() = homeFragment()
    }

    val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(homeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime = view.findViewById<TextView>(R.id.currentTime)
        viewModel.currentDate.observe(viewLifecycleOwner, Observer {
            currentTime.text = viewModel.currentDate.value
        })
    }
}