package com.example.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.search.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up RecyclerView
        searchAdapter = SearchAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = searchAdapter
        }

        // Debouncing the search input using Flow
        lifecycleScope.launch {
            setupSearch()
        }
    }

    private suspend fun setupSearch() {
        binding.searchEditText.textChanges()
            .debounce(300)  // Delay of 300ms to debounce
            .filter { query -> query.isNotEmpty() }  // Ignore empty inputs
            .flatMapLatest { query -> search(query) }  // New search with each new query
            .collect { results ->
                searchAdapter.submitList(results)
            }
    }

    // Simulated search function
    private fun search(query: String): Flow<List<String>> = flow {
        delay(500) // Simulating a network/database delay
        val results = getSearchResults(query)
        emit(results)
    }

    // Simulated data source
    private fun getSearchResults(query: String): List<String> {
//        val allResults = listOf("Apple", "Banana", "Orange", "Grape", "Pineapple", "Mango", "Strawberry")
        val allResults = listOf("iphone", "iphone 16", "iphone 15 pro max", "iphone charger", "iphone SE", "Iphone magsafe", "iphone china copy")
        return allResults.filter { it.contains(query, ignoreCase = true) }
    }
}
