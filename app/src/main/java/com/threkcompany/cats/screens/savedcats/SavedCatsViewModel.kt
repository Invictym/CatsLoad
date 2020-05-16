package com.threkcompany.cats.screens.savedcats

import androidx.lifecycle.ViewModel
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import com.threkcompany.cats.screens.cats.CatsListViewModel
import kotlinx.coroutines.*

class SavedCatsViewModel(val listener: CatsListViewModel.Listener, val db: CatsDatabaseDao) :
    ViewModel() {

    val cats = db.getCats()

    private var viewModelJob = Job()
    private lateinit var selectedCat: Cat
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun clickOnCat(cat: Cat) {
        selectedCat = cat
        listener.showDialog()
    }

    fun dialogResult(isOk: Boolean) {
        if (isOk) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    db.delete(selectedCat)
                }
            }
        }
    }

}
