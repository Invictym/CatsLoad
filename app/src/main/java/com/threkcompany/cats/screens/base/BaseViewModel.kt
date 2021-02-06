package com.threkcompany.cats.screens.base

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.LocalFileWorker
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import kotlinx.coroutines.*

open class BaseViewModel(private val db: CatsDatabaseDao,
                         private val context: Context,
                         private val path: String,
                         private val callback : MessageCallBack) : ViewModel()  {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun dialogResult(cat: Cat) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                db.insert(cat)
            }
        }
    }

    fun dialogResult(bitmap: Bitmap) {
        val name = "cat_${System.currentTimeMillis()}"
        uiScope.launch {
            withContext(Dispatchers.IO) {
                LocalFileWorker().saveImageToDir(path,  name, bitmap, context)
            }
        }
        callback.callBack("Saved to $path/$name")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    interface MessageCallBack {
        fun callBack(message: String)
    }
}