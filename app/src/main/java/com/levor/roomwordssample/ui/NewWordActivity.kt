package com.levor.roomwordssample.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.levor.roomwordssample.R
import com.levor.roomwordssample.entity.Word
import com.levor.roomwordssample.viewModel.NewWordViewModel
import kotlinx.android.synthetic.main.activity_new_word.*

class NewWordActivity : AppCompatActivity() {

    private lateinit var newWordViewModel: NewWordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        newWordViewModel = ViewModelProviders.of(this).get(NewWordViewModel::class.java)

        val currentWordId = intent.getIntExtra(INCOMING_WORD_ID, -1)
        if (currentWordId < 0) {
            onWordLoaded(Word(""))
        } else {
            newWordViewModel.loadWord(currentWordId) { word ->
                onWordLoaded(word)
            }
        }
    }

    private fun onWordLoaded(word: Word) {
        edit_word.setText(word.word)

        button_save.setOnClickListener {
            val input = edit_word.text.toString()
            newWordViewModel.saveWord(word.copy(word = input))
            finish()
        }
    }

    companion object {
        private const val INCOMING_WORD_ID = "com.example.android.roomwordssample.INCOMING_WORD_ID"

        fun startActivity(context: Context, wordId: Int? = null) {
            val intent = Intent(context, NewWordActivity::class.java)
            intent.putExtra(INCOMING_WORD_ID, wordId)
            context.startActivity(intent)
        }
    }
}
