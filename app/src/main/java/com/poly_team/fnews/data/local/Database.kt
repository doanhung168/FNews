package com.poly_team.fnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poly_team.fnews.data.model.News

@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

}