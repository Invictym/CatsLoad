package com.threkcompany.cats.screens.cats

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.logic.enternet.SearchCatsProvider
import com.threkcompany.cats.screens.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
class CatsListViewModel(
    context: Context,
    path: String,
    db: CatsDatabaseDao,
    callback: MessageCallBack
) : BaseViewModel(db, context, path, callback) {

    private val _cats = MutableLiveData<ArrayList<Cat>>()
    private val imageCount = 8
    val cats: LiveData<ArrayList<Cat>>
        get() = _cats

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
}