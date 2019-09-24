package com.levor.roomwordssample.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.levor.roomwordssample.R
import com.levor.roomwordssample.WordViewModel
import com.levor.roomwordssample.entity.Word
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var wordViewModel: WordViewModel
    private lateinit var adapter: WordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // TODO check view model factory approach
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        initRecyclerView()

        fab.setOnClickListener {
            createNewWord()
        }
    }

    private fun initRecyclerView() {
        adapter = WordListAdapter(::editWord)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel.getAllWords().observe(this, Observer<List<Word>> { words ->
            adapter.submitList(words)
        })

        val touchHelper = ItemTouchHelper(
            object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val word = adapter.getWordByPosition(viewHolder.adapterPosition)
                    Toast.makeText(this@MainActivity, "Deleting " + word.word, Toast.LENGTH_LONG).show()
                    wordViewModel.deleteWord(word)
                }

            }
        )
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.clear_data -> {
                Toast.makeText(this, "Clearing the data...", Toast.LENGTH_SHORT).show()
                wordViewModel.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                NEW_WORD_ACTIVITY_REQUEST_CODE -> {
                    data?.getParcelableExtra<Word>(NewWordActivity.EXTRA_REPLY)?.let {
                        wordViewModel.insert(it)
                    }
                }
                EDIT_WORD_ACTIVITY_REQUEST_CODE -> {
                    data?.getParcelableExtra<Word>(NewWordActivity.EXTRA_REPLY)?.let {
                        wordViewModel.update(it)
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, R.string.empty_not_saved, Toast.LENGTH_LONG).show()
        }
    }

    private fun createNewWord() {
        NewWordActivity.startActivityForResult(this, NEW_WORD_ACTIVITY_REQUEST_CODE)
    }

    private fun editWord(word: Word) {
        NewWordActivity.startActivityForResult(this, EDIT_WORD_ACTIVITY_REQUEST_CODE, word)
    }

    companion object {
        private const val NEW_WORD_ACTIVITY_REQUEST_CODE = 1
        private const val EDIT_WORD_ACTIVITY_REQUEST_CODE = 2
    }
}
