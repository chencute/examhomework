package com.zt.endexam.ui.music

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zt.endexam.R


class musicListFragment : Fragment() {

    companion object {
        fun newInstance() = musicListFragment()
    }

    private lateinit var adapter: MusicAdapter

    //创建viewModel
    val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MusicViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        //将请求后的数据放于适配器中
        val fragment_p = MusicFragment()
//        fragment_p.closeDrawerLayout()
        adapter = MusicAdapter(fragment_p,viewModel.getMusicData())
        recyclerView.adapter = adapter
    }

}