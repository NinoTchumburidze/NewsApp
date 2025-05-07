package ge.nchumburidze.newsapp.DataClasses

import NewsResponse
import SingleNewsResponse
import ge.nchumburidze.newsapp.api.ApiService
import retrofit2.Response

class Repository(private val apiService: ApiService, private val apiKey: String) {

    suspend fun getContent(
        query: String? = null,
        section: String? = null,
        fromDate: String? = null,
        toDate: String? = null
    ): Response<NewsResponse> {
        return apiService.getContent(query, section, fromDate, toDate, apiKey = apiKey)
    }

    suspend fun getArticleById(id: String): Response<SingleNewsResponse> {
        return apiService.getArticleById(id, apiKey = apiKey)
    }
}
