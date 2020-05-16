package com.threkcompany.cats.logic.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.threkcompany.cats.entity.Cat

@Dao
interface CatsDatabaseDao {

    @Insert
    fun insert(cat: Cat)

    @Delete
    fun delete(cat: Cat)

    @Update
    fun update(cat: Cat)

    @Query("SELECT * FROM cat_table")
    fun getCats(): LiveData<List<Cat>>

}