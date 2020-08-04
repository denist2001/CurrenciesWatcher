package com.codechallenge.revolutcodechallenge.repository

import com.codechallenge.revolutcodechallenge.response.Response
import io.reactivex.rxjava3.core.Observable

interface Repository {
    fun getValuesFor(currency: String) : Observable<Response>
}
