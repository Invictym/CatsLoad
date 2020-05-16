package com.threkcompany.cats.screens.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import java.lang.IllegalArgumentException

class CatsListViewModelFactory(val listener: CatsListViewModel.Listener, val db: CatsDatabaseDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsListViewModel::class.java)) {
            return CatsListViewModel(listener, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}