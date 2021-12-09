package id.umma.prayertimes.ahmadasrori.di

import id.umma.prayertimes.ahmadasrori.data.Repository
import id.umma.prayertimes.ahmadasrori.remote.RetrofitClient
import org.koin.dsl.module

val dataModule = module {
    single {
        RetrofitClient.instance
    }
    factory {
        Repository(get())
    }
}