package com.example.mysets

import android.app.Application
import com.example.mysets.BuildConfig.KEY_API
import com.example.mysets.network.LegoApiService
import com.example.mysets.repositories.Repository
import com.example.mysets.view.model.LegoViewModelFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application(), KodeinAware {
    companion object {
        private const val BASE_URL = "https://rebrickable.com/api/v3/lego/"
    }

    override val kodein by Kodein.lazy {
        bind() from singleton {
            Interceptor { chain ->
                val newUrl = chain
                    .request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("key", KEY_API)
                    .build()
                val newRequest = chain
                    .request()
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }
        }

        bind() from singleton {
            OkHttpClient()
                .newBuilder()
                .addInterceptor(instance())
                .build()
        }

        bind() from singleton {
            Retrofit.Builder()
                .client(instance())
                .baseUrl(BASE_URL)
                .addConverterFactory(instance())
                .addCallAdapterFactory(instance())
                .build()
        }

        bind() from singleton { GsonConverterFactory.create() }
        bind() from singleton { CoroutineCallAdapterFactory() }

        bind() from singleton { instance<Retrofit>().create(LegoApiService::class.java) }
        bind() from singleton { Repository(instance()) }
        bind() from singleton { LegoViewModelFactory(instance()) }
    }
}