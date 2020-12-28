package com.zt.endexam.ui.game



import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zt.endexam.R

class dialogScore : androidx.fragment.app.DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = layoutInflater.inflate(R.layout.scoredisplay,null)
        return view
    }
}