package com.mikirinkode.openstreetmapsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mikirinkode.openstreetmapsample.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val searchProvider = SearchProvider()

        binding.btnSearch.setOnClickListener {
            Toast.makeText(this, "Search/${binding.edtStartLocation.text.toString()}", Toast.LENGTH_SHORT).show()
            searchProvider.search(binding.edtStartLocation.text.toString().trim())
        }
    }
}