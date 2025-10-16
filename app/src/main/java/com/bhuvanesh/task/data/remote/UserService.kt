package com.bhuvanesh.task.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface UserService {

    @GET("/")
    fun getUserData(): Call<Unit>
}