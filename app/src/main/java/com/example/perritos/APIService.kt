package com.example.perritos

import retrofit2.Response
import retrofit2.http.*

interface APIService {
    //metodo por el cual se consume el servicio
    @GET("breed/{bread}/images")
    suspend fun getDogsByBreeds(@Path("bread") bread:String):Response<DogsResponse>

}