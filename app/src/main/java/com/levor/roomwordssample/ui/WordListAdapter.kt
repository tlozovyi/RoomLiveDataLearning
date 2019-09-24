package com.levor.roomwordssample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.levor.roomwordssample.R
import com.levor.roomwordssample.entity.Word

class WordListAdapter(
    val onWordClicked: (Word) -> Unit
): ListAdapter<Word, WordListAdapter.WordViewHolder>(WordItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.wordTitle.text = getItem(position).word
        holder.wordTitle.setOnClickListener {
            onWordClicked.invoke(getItem(holder.adapterPosition))
        }
    }

    fun getWordByPosition(position: Int) = getItem(position)

    class WordViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.word_recycler_view_item, parent, false)
    ) {
        val wordTitle: TextView = itemView.findViewById(R.id.textView)
    }

    class WordItemCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return true
        }

    }
}