package com.packt.newsfeed.di

import com.packt.newsfeed.data.NewsFeedRemoteDataSource
import com.packt.newsfeed.data.NewsFeedRepositoryImpl
import com.packt.newsfeed.data.RetrofitInstance
import com.packt.newsfeed.domain.GetTheNewsFeedUseCase
import com.packt.newsfeed.domain.NewsFeedRepository
import com.packt.newsfeed.ui.NewsFeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsFeedModule = module {
    single { RetrofitInstance.getNewsFeedApi() }
    single { NewsFeedRemoteDataSource(get()) }
    single<NewsFeedRepository> { NewsFeedRepositoryImpl(get()) }
    factory { GetTheNewsFeedUseCase(get()) }
    viewModel<NewsFeedViewModel>()
}