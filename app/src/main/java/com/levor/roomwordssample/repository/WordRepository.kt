package com.levor.roomwordssample.repository

import com.levor.roomwordssample.db.WordRoomDatabase
import com.levor.roomwordssample.entity.Word
import com.levor.roomwordssample.dao.WordDao
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WordRepository {
    private val wordDao = WordRoomDatabase.getInstance().wordDao()
    private val allWordsLiveData = wordDao.getAllWords()

    fun getAllWords() = allWordsLiveData

    suspend fun insert(word: Word) = withContext(Dispatchers.IO) {
        wordDao.insert(word)
    }

    suspend fun update(word: Word) = withContext(Dispatchers.IO) {
        wordDao.update(word)
    }

    suspend fun upsert(word: Word) = withContext(Dispatchers.IO) {
        wordDao.upsert(word)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        wordDao.deleteAll()
    }

    suspend fun deleteWord(word: Word) = withContext(Dispatchers.IO) {
        wordDao.deleteWord(word)
    }

    suspend fun getWordById(wordId: Int) = withContext(Dispatchers.IO) {
        wordDao.getWordById(wordId)
    }
}