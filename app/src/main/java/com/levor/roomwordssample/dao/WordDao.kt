package com.levor.roomwordssample.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.levor.roomwordssample.entity.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM word_table LIMIT 1")
    fun getAnyWord(): List<Word>
}