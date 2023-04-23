package com.poly_team.fnews.data.repository

import android.app.Application
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.data.network.NEWS
import com.poly_team.fnews.data.network.Network
import com.poly_team.fnews.data.network.NewsPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 10

class NewsRepository @Inject constructor(
    private val mApp: Application,
    private val mNetwork: Network,
    private val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

      fun getNewsByField(field: String): Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE),
            pagingSourceFactory = { NewsPagingSource(mNetwork, field, NEWS) }
        ).flow


}