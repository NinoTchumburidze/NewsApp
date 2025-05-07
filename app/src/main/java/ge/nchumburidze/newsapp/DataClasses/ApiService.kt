package ge.nchumburidze.newsapp.api

import NewsResponse
import SingleNewsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun getContent(
        @Query("q") query: String? = null,
        @Query("section") section: String? = null,
        @Query("from-date") fromDate: String? = null,
        @Query("to-date") toDate: String? = null,
        @Query("show-fields") showFields: String = "headline,standfirst,body,thumbnail",
        @Query("api-key") apiKey: String,
        @Query("page-size") pageSize: Int = 20
    ): Response<NewsResponse>

    @GET("{id}")
    suspend fun getArticleById(
        @retrofit2.http.Path("id", encoded = true) id: String,
        @Query("show-fields") showFields: String = "headline,standfirst,body,thumbnail",
        @Query("api-key") apiKey: String
    ): Response<SingleNewsResponse>
}