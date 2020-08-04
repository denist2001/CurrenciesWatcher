package com.codechallenge.revolutcodechallenge

import com.codechallenge.revolutcodechallenge.ui.main.Presentations
import java.util.*
import javax.inject.Inject

class Calculator @Inject constructor() {

    fun calculate(rates: HashMap<String, Float>, coefficientTransformation: Float): List<Presentations> {
        val convertedRates = mutableListOf<Presentations>()
        for (rate in rates) {
            val countedValue = rate.value * coefficientTransformation
            convertedRates.add(Presentations(
                rate.key,
                String.format("%.2f", countedValue)
            ))
        }
        return convertedRates
    }

}
