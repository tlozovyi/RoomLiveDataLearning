package com.levor.roomwordssample.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.levor.roomwordssample.App
import com.levor.roomwordssample.dao.WordDao
import com.levor.roomwordssample.entity.Word
import kotlinx.coroutines.*

@Database(entities = [Word::class], version = 3, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        private val dbInstance: WordRoomDatabase by lazy {
            Room.databaseBuilder(
                App.instance,
                WordRoomDatabase::class.java,
                "word_database"
            )
                .fallbackToDestructiveMigration()
                .addCallback(DbCallback())
                .build()
        }

        private class DbCallback : Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                GlobalScope.launch(Dispatchers.IO) {
                    val dao = dbInstance.wordDao()
                    if (dao.getAnyWord().isEmpty()) {
                        dao.deleteAll()
                        val words = listOf("dolphin", "crocodile", "cobra")
                        words.forEach {
                            dao.insert(Word(it))
                        }
                    }
                }
            }
        }

        fun getInstance() = dbInstance
    }
}