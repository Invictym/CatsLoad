package com.threkcompany.cats.logic.enternet

import com.threkcompany.cats.entity.Cat
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CatsApiService {
    @Headers("x-api-key: 19e81a07-ca07-4968-b54a-8f4a059bc103")
    @GET("v1/images/search")
    fun search(@Query("limit") limit: Int) : Observable<ArrayList<Cat>>

    companion object Factory {
        fun create(): CatsApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.thecatapi.com/")
                .build()
            return retrofit.create(CatsApiService::class.java)
        }
    }
}

class SearchCats(val service: CatsApiService) {
    fun searchCats(limit: Int) : Observable<ArrayList<Cat>> {
        return service.search(limit)
    }
}

object SearchCatsProvider {
    fun provideSearchCats() :SearchCats {
        return SearchCats(CatsApiService.create())
    }
}