package com.codechallenge.revolutcodechallenge.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codechallenge.revolutcodechallenge.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var mainFragmentAdapter: MainFragmentAdapter
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(currencies_rv) {
            adapter = mainFragmentAdapter
            layoutManager = LinearLayoutManager(context)
        }
//        mainFragmentAdapter.
    }
}