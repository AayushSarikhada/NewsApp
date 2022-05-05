package com.example.network.Interfaces

import com.example.models.newsFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface newsInterface {

    @GET("item/{id}.json")
    fun getPosts(@Path("id")a:Int): Call<newsFeed>

    @GET("topstories.json")
    fun getTopStories():Call<List<Int>>

}