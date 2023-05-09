package com.poly_team.fnews.data.local

import androidx.room.*
import com.poly_team.fnews.data.model.News

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: ArrayList<News>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: News)

    @Query("SELECT * FROM news")
    fun getAll(): List<News>

    @Query("SELECT * FROM news WHERE id = :newsId")
    fun getNewsById(newsId: String): News

    @Query("DELETE FROM news WHERE id NOT IN (:newsId)")
    fun deleteAllExpect(newsId: String)

    @Query("DELETE FROM news")
    fun deleteAll()

}