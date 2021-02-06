package com.threkcompany.cats.screens.savedcats

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.screens.base.BaseViewModel
import kotlinx.coroutines.*

class SavedCatsViewModel(
    context: Context,
    path: String,
    private val db: CatsDatabaseDao,
    callback: MessageCallBack
) : BaseViewModel(db, context, path, callback) {
    private val _cats = MutableLiveData<List<Cat>>()
    val cats : LiveData<List<Cat>>
        get() = _cats
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        loadCats()
    }

    private fun loadCats() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _cats.postValue(db.getCats())
            }
        }
    }

    fun dialogResultRemove(cat: Cat) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                db.delete(cat)
                loadCats()
            }
        }
    }
}
