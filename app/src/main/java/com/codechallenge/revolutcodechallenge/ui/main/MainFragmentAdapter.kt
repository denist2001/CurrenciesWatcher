package com.codechallenge.revolutcodechallenge.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.codechallenge.revolutcodechallenge.FlagLink
import com.codechallenge.revolutcodechallenge.LinksToFlagsPictures
import com.codechallenge.revolutcodechallenge.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainFragmentAdapter @Inject constructor() :
    RecyclerView.Adapter<MainFragmentAdapter.MainFragmentsViewHolder>() {

    private val currenciesList =
        LinksToFlagsPictures.flagsList.subList(1, LinksToFlagsPictures.flagsList.lastIndex)
    private var rates: HashMap<String, String> = hashMapOf()
    private lateinit var mainCurrencyChanged: (String) -> Unit
    private var currentTopCurrency = LinksToFlagsPictures.flagsList[0]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_tv_item, parent, false)
        return MainFragmentsViewHolder(view)
    }

    override fun getItemCount(): Int = currenciesList.size

    override fun onBindViewHolder(holder: MainFragmentsViewHolder, position: Int) {
        val flagLink = currenciesList[position]
        holder.title.text = flagLink.name
        holder.description.text = flagLink.description
        holder.image.load(flagLink.link) {
            scale(Scale.FIT)
            placeholder(R.drawable.ic_baseline_outlined_flag_24)
            transformations(CircleCropTransformation())
        }
        holder.value.setText(getValueFor(flagLink.name))
        changeValue(holder.value, flagLink.name)
        holder.itemView.setOnClickListener {
            changePositions(position)
        }
    }

    private fun getValueFor(currencyName: String): String {
        if (rates.containsKey(currencyName)) {
            return rates[currencyName]!!
        }
        return ""
    }

    private fun changeValue(
        editTextView: EditText,
        currencyName: String
    ) {
        if (rates.isEmpty()) return
        val editText = editTextView.editableText
        editText.clear()
        editTextView.setText(rates[currencyName])
    }

    private fun changePositions(position: Int) {
        val flagLink = currenciesList[position]
        currenciesList.removeAt(position)
        currenciesList.add(0, currentTopCurrency)
        currentTopCurrency = flagLink
        notifyDataSetChanged()
        mainCurrencyChanged(flagLink.name)
    }

    fun setRates(presentations: List<Presentations>) {
        rates.clear()
        for (presentation in presentations) {
            rates[presentation.currency] = presentation.rate
        }
        notifyDataSetChanged()
    }

    fun setMainCurrencyChanged(listener: (String) -> Unit) {
        this.mainCurrencyChanged = listener
    }

    fun getCurrentTopCurrency(): FlagLink = currentTopCurrency

    class MainFragmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val image: ImageView = itemView.findViewById(R.id.label_image)
        val value: EditText = itemView.findViewById(R.id.input)
    }
}