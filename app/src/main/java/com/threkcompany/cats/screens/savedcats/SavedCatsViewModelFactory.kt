package com.threkcompany.cats.screens.savedcats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.threkcompany.cats.logic.db.CatsDatabaseDao

class SavedCatsViewModelFactory(private val context: Context,
                                private val path: String,
                                private val db: CatsDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedCatsViewModel::class.java)) {
            return SavedCatsViewModel(context, path, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}