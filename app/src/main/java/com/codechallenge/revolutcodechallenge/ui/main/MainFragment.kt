package com.codechallenge.revolutcodechallenge.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.revolutcodechallenge.FlagLink
import com.codechallenge.revolutcodechallenge.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment), LifecycleOwner {

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
        subscribeOnViewModelState()
        viewModel.onAction(
            ViewModelAction
                .StartRefreshing(mainFragmentAdapter.getCurrentTopCurrency().name)
        )
        mainFragmentAdapter.setMainCurrencyChanged { newCurrency ->
            viewModel.onAction(ViewModelAction.StartRefreshing(newCurrency))
            refreshTopCurrencyView(mainFragmentAdapter.getCurrentTopCurrency())
        }
        refreshTopCurrencyView(mainFragmentAdapter.getCurrentTopCurrency())
    }

    private fun refreshTopCurrencyView(flagLink: FlagLink) {
        main_title.text = flagLink.name
        main_description.text = flagLink.description
        (main_image as ImageView).load(flagLink.link) {
            scale(Scale.FIT)
            placeholder(R.drawable.ic_baseline_outlined_flag_24)
            transformations(CircleCropTransformation())
        }
        (main_input as EditText).addTextChangedListener(textChangeListener)
    }

    private fun subscribeOnViewModelState() {
        viewModel.state.observe(viewLifecycleOwner,
            Observer<ViewModelState> { state ->
                when (state) {
                    ViewModelState.Loading -> {
                        showProgressBar(true)
                    }
                    is ViewModelState.Error -> {
                        showProgressBar(false)
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is ViewModelState.Result -> {
                        showProgressBar(false)
                        mainFragmentAdapter.setRates(state.presentations)
                    }
                }
            })
    }

    private fun showProgressBar(isVisible: Boolean) {
        when (isVisible) {
            true -> {
                progressBar.visibility = View.VISIBLE
            }
            false -> {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(newValue: CharSequence?, start: Int, before: Int, count: Int) {
            if (newValue.isNullOrEmpty()) return
            val newCoefficient = newValue.toString().toFloat()
            viewModel.onAction(ViewModelAction.CoefficientChanges(newCoefficient))
        }

    }
}