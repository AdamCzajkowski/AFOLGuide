package com.application.afol

import android.app.Application
import androidx.room.Room
import com.application.afol.BuildConfig.KEY_API
import com.application.afol.database.MySetsDatabase
import com.application.afol.network.LegoApiService
import com.application.afol.repositories.Repository
import com.application.afol.vm.bricksListViewModel.BrickListViewModel
import com.application.afol.vm.bricksListViewModel.BrickListViewModelFactory
import com.application.afol.vm.detailViewModel.DetailViewModel
import com.application.afol.vm.detailViewModel.DetailViewModelFactory
import com.application.afol.vm.mySetsViewModel.MySetsViewModel
import com.application.afol.vm.mySetsViewModel.MySetsViewModelFactory
import com.application.afol.vm.searchBrickViewModel.SearchBrickViewModel
import com.application.afol.vm.searchBrickViewModel.SearchBrickViewModelFactory
import com.application.afol.vm.searchViewModel.SearchLegoViewModel
import com.application.afol.vm.searchViewModel.SearchLegoViewModelFactory
import com.application.afol.vm.themeActivityViewModel.ThemeActivityViewModel
import com.application.afol.vm.themeActivityViewModel.ThemeActivityViewModelFactory
import com.application.afol.vm.themesViewModel.ThemesViewModel
import com.application.afol.vm.themesViewModel.ThemesViewModelFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
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

        bind() from singleton { GsonConverterFactory.create() }
        bind() from singleton { CoroutineCallAdapterFactory() }
        bind() from singleton { ThemesViewModel(instance()) }
        bind() from singleton { ThemesViewModelFactory(instance()) }
        bind() from singleton { BrickListViewModel(instance()) }
        bind() from singleton { BrickListViewModelFactory(instance()) }
        bind() from singleton { DetailViewModelFactory(instance()) }
        bind() from singleton { DetailViewModel(instance()) }
        bind() from singleton { MySetsViewModel(instance()) }
        bind() from singleton { SearchLegoViewModel(instance()) }
        bind() from singleton { MySetsViewModelFactory(instance()) }
        bind() from singleton { SearchLegoViewModelFactory(instance()) }
        bind() from singleton { instance<Retrofit>().create(LegoApiService::class.java) }
        bind() from singleton { Repository(instance(), instance()) }
        bind() from singleton { ThemeActivityViewModel(instance()) }
        bind() from singleton { ThemeActivityViewModelFactory(instance()) }
        bind() from singleton { SearchBrickViewModel(instance()) }
        bind() from singleton { SearchBrickViewModelFactory(instance()) }
    }
}