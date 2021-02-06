package com.threkcompany.cats.screens.savedcats

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.LocalFileWorker
import com.threkcompany.cats.logic.db.CatsDatabaseDao
import kotlinx.coroutines.*

class SavedCatsViewModel(
    private val context: Context,
    private val path: String,
    private val db: CatsDatabaseDao
) : ViewModel() {

    val cats = db.getCats()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun dialogResult(cat: Cat) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                db.delete(cat)
            }
        }
    }

    fun dialogResult(bitmap: Bitmap) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                LocalFileWorker().saveImageToDir(
                    path, "cat_${System.currentTimeMillis()}",
                    bitmap, context
                )
            }
        }
    }
}
