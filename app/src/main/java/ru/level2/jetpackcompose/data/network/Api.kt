package ru.level2.jetpackcompose.data.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.level2.jetpackcompose.entities.Episode
import ru.level2.jetpackcompose.entities.Response

interface Api {
    @GET("/api/character")
    suspend fun loadList(@Query("page") page: Int): retrofit2.Response<Response>

    @GET("api/episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): retrofit2.Response<Episode>

    companion object {
        const val pageSize = 20

        val retrofit by lazy {
            Retrofit
                .Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create<Api>()
        }
    }
}