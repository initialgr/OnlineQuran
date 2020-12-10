package com.app.muslim

import com.app.muslim.models.Data
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface QuranApi {

    @GET("/v1/quran/ar.alafasy")
    suspend fun getSurat(
        @QueryMap queries: Map<String, String>
    ): Response<Data>

}