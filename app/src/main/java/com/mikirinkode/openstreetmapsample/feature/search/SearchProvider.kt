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
//                    Log.e("Search", response.message())
//                    Log.e("Search", response.body()?.size.toString())
//                    Log.e("Search", response.body().toString())
//                    Log.e("Search", response.body()?.get(0).toString())
//                    Log.e("Search", response.body()?.get(0)?.displayName.toString())
//                    Log.e("Search", response.body()?.get(0)?.lat.toString())
//                    Log.e("Search", response.body()?.get(0)?.lon.toString())
                    response.body()?.let { view.setData(it) }
                } else {
                    Log.e("Search", response.message())
                    Log.e("Search", response.body().toString())
                }
            }
        }
    }
}