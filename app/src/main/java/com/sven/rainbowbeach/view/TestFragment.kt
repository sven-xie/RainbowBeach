package com.sven.rainbowbeach.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sven.rainbowbeach.R

/**
 * @Author:         xwp
 * @CreateDate:     2023/7/10
 * @Version:        1.0
 */
class TestFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }
}