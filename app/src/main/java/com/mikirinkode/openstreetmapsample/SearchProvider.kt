package com.mikirinkode.openstreetmapsample

import android.text.Editable
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchProvider {

    fun search(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.getApiService().search(query, "json")
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    Log.e("Search", response.message())
                    Log.e("Search", response.body()?.size.toString())
                    Log.e("Search", response.body().toString())
                    Log.e("Search", response.body()?.get(0).toString())
                    Log.e("Search", response.body()?.get(0)?.displayName.toString())
                    Log.e("Search", response.body()?.get(0)?.lat.toString())
                    Log.e("Search", response.body()?.get(0)?.lon.toString())
                } else {
                    Log.e("Search", response.message())
                    Log.e("Search", response.body().toString())
                }
            }
        }
    }
}