package com.threkcompany.cats.screens.savedcats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.screens.base.BaseViewModel

class SavedCatsViewModelFactory(private val context: Context,
                                private val path: String,
                                private val db: CatsDatabaseDao,
                                private val callback: BaseViewModel.MessageCallBack) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedCatsViewModel::class.java)) {
            return SavedCatsViewModel(context, path, db, callback) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}