package com.app.muslim.data

import com.app.muslim.data.network.QuranApi
import com.app.muslim.models.Data
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val quranApi: QuranApi
) {

    suspend fun getSurat(queries: Map<String, String>): Response<Data> {
        return quranApi.getSurat(queries)
    }
}