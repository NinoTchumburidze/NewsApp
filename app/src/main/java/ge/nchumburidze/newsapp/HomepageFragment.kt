package ge.nchumburidze.newsapp

import Category
import CategoryAdapter
import NewsDetailFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.nchumburidze.newsapp.DataClasses.Repository
import ge.nchumburidze.newsapp.api.RetrofitClient
import ge.nchumburidze.newsapp.databinding.HomepageFragmentBinding
import kotlinx.coroutines.launch
import kotlin.jvm.java

class HomepageFragment : Fragment(R.layout.homepage_fragment) {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsRecyclerView: RecyclerView
    private var _binding: HomepageFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomepageFragmentBinding.inflate(inflater, container, false)

        // Setup category RecyclerView
        categoryRecyclerView = binding.categories
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val categories = listOf(
            Category("business", "Business"),
            Category("politics", "Politics"),
            Category("artanddesign", "Art and design"),
            Category("world", "World news"),
            Category("science", "Science"),
            Category("sport", "Sport"),
            Category("technology", "Technology"),
            Category("us-news", "US news")

        )

        categoryAdapter = CategoryAdapter(categories) { selectedCategory ->
            // When a category is clicked, fetch the articles for that category
            fetchArticlesForCategory(selectedCategory)
        }
        categoryRecyclerView.adapter = categoryAdapter

        // Setup news RecyclerView with empty adapter
        newsRecyclerView = binding.newsItems
        newsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        newsAdapter = NewsAdapter(emptyList()) { article ->
            val bundle = Bundle().apply {
                putString("title", article.webTitle)
                putString("body", article.fields.body)
                putString("thumbnail", article.fields.thumbnail)
            }

            val fragment = NewsDetailFragment()
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .addToBackStack(null)
                .commit()
        }
        newsRecyclerView.adapter = newsAdapter

        val apiService = RetrofitClient.create()
        val apiKey = "c8dcb073-93c4-4bbc-9b78-f5076e718b80"
        repository = Repository(apiService, apiKey)

        loadNewsItems()

        binding.searchImage.setOnClickListener {
            val query = binding.searchView.text.toString().trim()
            if (query.isNotEmpty()) {
                searchNews(query)
            }
        }


        return binding.root
    }

    private fun searchNews(query: String) {
        lifecycleScope.launch {
            try {
                val response = repository.getContent(section = null, query = query)
                if (response.isSuccessful) {
                    val articles = response.body()?.response?.results ?: emptyList()
                    newsAdapter.setArticles(articles)
                } else {
                    Log.e("HomepageFragment", "Search error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomepageFragment", "Search exception: ${e.message}")
            }
        }
    }



    private fun fetchArticlesForCategory(category: Category) {
        loadNewsItems(category.sectionId)
    }

    // Modify loadNewsItems to take sectionId
    private fun loadNewsItems(sectionId: String) {
        lifecycleScope.launch {
            try {
                val response = repository.getContent(section = sectionId, query = null)
                if (response.isSuccessful) {
                    val articles = response.body()?.response?.results ?: emptyList()
                    Log.d("HomepageFragment", "Fetched articles: ${articles.size}")

                    newsAdapter.setArticles(articles)
                } else {
                    Log.e("HomepageFragment", "Error: ${response.code()} - ${response.message()}")
                    Log.e("HomepageFragment", "Error body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomepageFragment", "Exception: ${e.message}")
            }
        }
    }


    private fun loadNewsItems() {
        lifecycleScope.launch {
            try {
                val response = repository.getContent(section = "news", query = null)
                if (response.isSuccessful) {
                    val articles = response.body()?.response?.results ?: emptyList()
                    Log.d("HomepageFragment", "Fetched articles: ${articles.size}")

                    newsAdapter.setArticles(articles)
                } else {
                    Log.e("HomepageFragment", "Error: ${response.code()} - ${response.message()}")
                    Log.e("HomepageFragment", "Error body: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("HomepageFragment", "Exception: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
