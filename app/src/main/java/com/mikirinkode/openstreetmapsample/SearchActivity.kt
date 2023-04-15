package com.mikirinkode.openstreetmapsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikirinkode.openstreetmapsample.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), SearchView {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val searchAdapter: SearchResultAdapter by lazy {
        SearchResultAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            rvResult.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvResult.adapter = searchAdapter
        }

        val searchProvider = SearchProvider(this)

        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Search/${binding.edtStartLocation.text.toString()}", Toast.LENGTH_SHORT).show()
            searchProvider.search(binding.edtStartLocation.text.toString().trim())
        }
    }

    override fun setData(list: List<SearchResultResponse>) {
        searchAdapter.setList(list)
    }
}