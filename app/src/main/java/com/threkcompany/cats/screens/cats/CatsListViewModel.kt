package com.threkcompany.cats.screens.cats

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.LocalFileWorker
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.logic.enternet.SearchCatsProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.io.File

@SuppressLint("CheckResult")
class CatsListViewModel(
    private val context: Context,
    private val path: String,
    private val db: CatsDatabaseDao
) : ViewModel() {

    private val _cats = MutableLiveData<ArrayList<Cat>>()
    private val imageCount = 8
    val cats: LiveData<ArrayList<Cat>>
        get() = _cats
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        val provider = SearchCatsProvider.provideSearchCats()
        provider.searchCats(imageCount)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result -> _cats.postValue(result) },
                { er -> Log.w("Cat load error", "$er") })
    }

    fun bindPosition(position: Int, size: Int) {
        if (position == size - 3) {
            SearchCatsProvider.provideSearchCats().searchCats(imageCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result -> _cats.value?.addAll(result) },
                    { er -> Log.w("Cat load error", "$er") })
        }
    }

    fun dialogResult(cat: Cat) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(cat)
            }
        }
    }

    fun dialogResult(bitmap: Bitmap) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                LocalFileWorker().saveImageToDir(path, "cat_${System.currentTimeMillis()}" , bitmap, context)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}