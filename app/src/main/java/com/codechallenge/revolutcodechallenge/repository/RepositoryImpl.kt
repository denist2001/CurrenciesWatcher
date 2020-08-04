package com.codechallenge.revolutcodechallenge.repository

import com.codechallenge.revolutcodechallenge.response.Response
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val service: RepositoryService) : Repository {
    override fun getValuesFor(currency: String): Observable<Response> {
        return service.getReviews(currency)
    }

}