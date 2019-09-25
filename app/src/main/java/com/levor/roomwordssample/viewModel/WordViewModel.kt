package com.levor.roomwordssample.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.levor.roomwordssample.entity.Word
import com.levor.roomwordssample.repository.WordRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class WordViewModel(app: Application) : AndroidViewModel(app), CoroutineScope {
    private val job = SupervisorJob()
    private val wordRepository = WordRepository()
    private val allWordsLiveData = wordRepository.getAllWords()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun getAllWords() = allWordsLiveData

    fun insert(word: Word) {
        launch {
            wordRepository.insert(word)
        }
    }

    fun update(word: Word) {
        launch {
            wordRepository.update(word)
        }
    }

    fun deleteAll() {
        launch {
            wordRepository.deleteAll()
        }
    }

    fun deleteWord(word: Word) {
        launch {
            wordRepository.deleteWord(word)
        }
    }
}