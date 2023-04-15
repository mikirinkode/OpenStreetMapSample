package com.mikirinkode.openstreetmapsample.feature.search

import android.util.Log
import com.mikirinkode.openstreetmapsample.data.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchProvider(private var view: SearchView) {

    fun search(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.getApiService().search(query, "json")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let { view.setData(it) }
                } else {
                    Log.e("Search", response.message())
                    Log.e("Search", response.body().toString())
                }
            }
        }
    }
}