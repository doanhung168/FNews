package com.poly_team.fnews.di

import com.poly_team.fnews.view.home.news.NewsAdapter
import com.poly_team.fnews.view.home.news.NewsViewPagerAdaptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideNewsAdapter() : NewsAdapter = NewsAdapter()

}