package com.example.perpustakaan.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface SeriesDao {

    /* ----------------------------------------------------------------------------------------- */
    // Getters

    @Query("SELECT * FROM Series ORDER BY uid ASC")
    suspend fun fetchAll(): List<Series>

    @Query("SELECT * FROM Series WHERE uid = :uid")
    suspend fun fetch(uid: Int): Series

    @RawQuery
    suspend fun fetch(query: SupportSQLiteQuery): List<Series>

    /* ----------------------------------------------------------------------------------------- */
    // Setters

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(series: Series)

    @Update
    suspend fun update(series: Series)

    /* ----------------------------------------------------------------------------------------- */
    // Removers

    @Delete
    suspend fun delete(series: Series)

    @Query("DELETE FROM Series WHERE uid = :uid")
    suspend fun delete(uid: Int)

    @Query("DELETE FROM Series")
    suspend fun nukeDatabase()

}