package com.example.mysets

import android.app.Application
import androidx.room.Room
import com.example.mysets.BuildConfig.KEY_API
import com.example.mysets.database.my.sets.MySetsDatabase
import com.example.mysets.database.wishlist.WishlistDatabase
import com.example.mysets.network.LegoApiService
import com.example.mysets.repositories.Repository
import com.example.mysets.ui.main.LegoRecyclerViewAdapter
import com.example.mysets.view.model.detailViewModel.DetailViewModel
import com.example.mysets.view.model.detailViewModel.DetailViewModelFactory
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModel
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModelFactory
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModel
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModelFactory
import com.example.mysets.view.model.wishlistViewModel.WishlistViewModel
import com.example.mysets.view.model.wishlistViewModel.WishlistViewModelFactory
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
        bind() from singleton {
            Room.databaseBuilder(applicationContext, MySetsDatabase::class.java, "mySets.db")
                .fallbackToDestructiveMigration()
                .build()
        }
        bind() from singleton {
            Room.databaseBuilder(applicationContext, WishlistDatabase::class.java, "wishlist.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        bind() from singleton { GsonConverterFactory.create() }
        bind() from singleton { CoroutineCallAdapterFactory() }
        bind() from provider { DetailViewModelFactory(instance()) }
        bind() from provider { DetailViewModel(instance()) }
        bind() from provider { MySetsViewModel(instance()) }
        bind() from provider { WishlistViewModel(instance()) }
        bind() from provider { SearchLegoViewModel(instance()) }
        bind() from provider { MySetsViewModelFactory(instance()) }
        bind() from provider { WishlistViewModelFactory(instance()) }
        bind() from provider { SearchLegoViewModelFactory(instance()) }
        bind() from singleton { instance<Retrofit>().create(LegoApiService::class.java) }
        bind() from singleton { Repository(instance(), instance(), instance()) }
    }
}