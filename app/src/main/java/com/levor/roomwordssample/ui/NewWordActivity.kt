package com.levor.roomwordssample.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.levor.roomwordssample.R
import com.levor.roomwordssample.entity.Word
import kotlinx.android.synthetic.main.activity_new_word.*

class NewWordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        val currentWord = intent.getParcelableExtra<Word>(INCOMING_WORD) ?: Word("")
        edit_word.setText(currentWord.word)

        button_save.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(edit_word.text.toString())) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val input = edit_word.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, currentWord.copy(word = input))
                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.roomwordssample.REPLY"

        private const val INCOMING_WORD = "com.example.android.roomwordssample.INCOMING_WORD"

        fun startActivityForResult(activity: Activity, requestCode: Int, word: Word? = null) {
            val intent = Intent(activity, NewWordActivity::class.java)
            intent.putExtra(INCOMING_WORD, word)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}
