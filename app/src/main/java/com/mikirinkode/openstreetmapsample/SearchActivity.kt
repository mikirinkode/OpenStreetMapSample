package com.mikirinkode.openstreetmapsample

import android.app.appsearch.SearchResult
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikirinkode.openstreetmapsample.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity(), SearchView {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val searchAdapter: SearchResultAdapter by lazy {
        SearchResultAdapter()
    }

    private val searchProvider: SearchProvider by lazy {
        SearchProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            rvResult.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvResult.adapter = searchAdapter
        }

        binding.edtStartLocation.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                showLoading()
                searchProvider.search(p0.toString().trim())
            }

            override fun afterTextChanged(p0: Editable?) {
//                TODO("Not yet implemented")
            }

        })

        clickAction()
    }

    override fun setData(list: List<SearchResultResponse>) {
        searchAdapter.setList(list)
        hideLoading()
    }

    private fun clickAction(){
        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Search/${binding.edtStartLocation.text.toString()}", Toast.LENGTH_SHORT).show()
            searchProvider.search(binding.edtStartLocation.text.toString().trim())
            showLoading()
        }

        searchAdapter.onItemClick = { response ->
            val intent = Intent()
            intent.putExtra(MainActivity.EXTRA_ADDRESS, response.displayName)
            intent.putExtra(MainActivity.EXTRA_LATITUDE, response.lat)
            intent.putExtra(MainActivity.EXTRA_LONGITUDE, response.lon)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
        super.onBackPressed()
    }
}