package com.mikirinkode.openstreetmapsample

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("search/{query}")
    suspend fun search(
        @Path("query") query: String,
        @Query("format") format: String
    ): Response<List<SearchResultResponse>>
}