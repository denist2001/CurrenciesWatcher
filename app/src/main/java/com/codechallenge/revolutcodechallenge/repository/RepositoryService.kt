package com.codechallenge.revolutcodechallenge.repository

import com.codechallenge.revolutcodechallenge.response.Response
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryService {
    @GET("/api/android/latest")
    fun getReviews(@Query("base") currency: String): Observable<Response>
}
