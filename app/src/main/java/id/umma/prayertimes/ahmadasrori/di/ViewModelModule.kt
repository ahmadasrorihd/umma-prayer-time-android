package id.umma.prayertimes.ahmadasrori.di

import id.umma.prayertimes.ahmadasrori.ui.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
}