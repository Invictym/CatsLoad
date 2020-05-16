package com.threkcompany.cats.screens.cats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.logic.db.CatsDb
import com.threkcompany.cats.logic.enternet.SearchCatsProvider
import com.threkcompany.cats.screens.cats.adapters.CatsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.text.FieldPosition

class CatsListViewModel(val listener: Listener, val db: CatsDatabaseDao) : ViewModel() {

    private val _cats = MutableLiveData<ArrayList<Cat>>()
    private val imageCount = 8
    val cats: LiveData<ArrayList<Cat>>
        get() =_cats

    lateinit var selectedCat: Cat

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    init {
        val provider = SearchCatsProvider.provideSearchCats()
        provider.searchCats(imageCount)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({result -> _cats.postValue(result)}, {er -> Log.w("Cat load error", "$er")})
    }

    fun clickOnCat(cat: Cat) {
        selectedCat = cat
        listener.showDialog()
    }

    fun bindPosition(position: Int, size: Int) {
        if (position == size - 3) {
            SearchCatsProvider.provideSearchCats().searchCats(imageCount)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({result -> _cats.value?.addAll(result)}, {er -> Log.w("Cat load error", "$er")})
        }
    }

    fun dialogResult(isOk: Boolean) {
        if (isOk) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    db.insert(selectedCat)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Listener(var function: () -> Unit) {
        fun showDialog() = function()
    }
}