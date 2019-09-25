package com.levor.roomwordssample.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.levor.roomwordssample.entity.Word
import com.levor.roomwordssample.repository.WordRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NewWordViewModel(app: Application): AndroidViewModel(app), CoroutineScope {

    private val job = SupervisorJob()
    private val wordRepository = WordRepository()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun loadWord(wordId: Int, onWordLoaded: (Word) -> Unit) {
        launch {
            val word = wordRepository.getWordById(wordId)
            onWordLoaded.invoke(word)
        }
    }

    fun saveWord(word: Word) {
        launch {
            wordRepository.upsert(word)
        }
    }
}