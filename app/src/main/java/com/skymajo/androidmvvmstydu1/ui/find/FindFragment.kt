package com.skymajo.androidmvvmstydu1.ui.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skymajo.androidmvvmstydu1.R
import com.skymajo.libnavannotation.FragmentDestination

@FragmentDestination(pathUrl = "main/tabs/find")
class FindFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_layout_publish, container, false)
    }
}