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
import com.codechallenge.revolutcodechallenge.LinksToFlagsPictures
import com.codechallenge.revolutcodechallenge.R
import javax.inject.Inject

class MainFragmentAdapter @Inject constructor() :
    RecyclerView.Adapter<MainFragmentAdapter.MainFragmentsViewHolder>() {

    private val currenciesList = LinksToFlagsPictures.flagsList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currency_tv_item, parent, false)
        return MainFragmentsViewHolder(view)
    }

    override fun getItemCount(): Int = currenciesList.size

    override fun onBindViewHolder(holder: MainFragmentsViewHolder, position: Int) {
        val flagLink = currenciesList[position]
        val currency = holder.value.editableText
        holder.title.text = flagLink.name
        holder.description.text = flagLink.description
        currency.clear()
        currency.insert(0, "1234.56")
        holder.image.load(flagLink.link) {
            scale(Scale.FIT)
            placeholder(R.drawable.ic_baseline_outlined_flag_24)
            transformations(CircleCropTransformation())
        }
    }

    class MainFragmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val description: TextView = itemView.findViewById(R.id.description)
        val image: ImageView = itemView.findViewById(R.id.label_image)
        val value: EditText = itemView.findViewById(R.id.input)
    }
}