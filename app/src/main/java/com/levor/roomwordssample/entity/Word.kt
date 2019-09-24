package com.levor.roomwordssample.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "word_table")
data class Word(
    @ColumnInfo(name = "word") val word: String,
    @PrimaryKey(autoGenerate = true) var identifier: Int = 0
): Parcelable