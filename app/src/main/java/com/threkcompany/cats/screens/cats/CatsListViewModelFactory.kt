package com.threkcompany.cats.screens.cats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import java.lang.IllegalArgumentException

class CatsListViewModelFactory(val context: Context, val path : String, val db: CatsDatabaseDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatsListViewModel::class.java)) {
            return CatsListViewModel(context, path, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}