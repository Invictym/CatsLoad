package com.threkcompany.cats.screens.savedcats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.logic.db.CatsDb
import com.threkcompany.cats.screens.cats.CatsListViewModel

class SavedCatsViewModelFactory(val listener: CatsListViewModel.Listener, val db: CatsDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedCatsViewModel::class.java)) {
            return SavedCatsViewModel(listener, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}