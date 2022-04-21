package com.example.translation.ui.home

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("/todos")
    fun getTodoList() : Call<JsonObject>
}