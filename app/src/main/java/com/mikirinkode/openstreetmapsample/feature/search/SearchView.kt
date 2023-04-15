package com.mikirinkode.openstreetmapsample.feature.search

import com.mikirinkode.openstreetmapsample.data.network.SearchResultResponse

interface SearchView {
    fun setData(list: List<SearchResultResponse>)
}