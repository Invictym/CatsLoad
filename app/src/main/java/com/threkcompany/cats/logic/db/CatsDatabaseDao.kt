package com.threkcompany.cats.logic.db

import androidx.room.*
import com.threkcompany.cats.entity.Cat

@Dao
interface CatsDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cat: Cat)

    @Delete
    fun delete(cat: Cat)

    @Update
    fun update(cat: Cat)

    @Query("SELECT * FROM cat_table")
    fun getCats(): List<Cat>

    @Query("SELECT * FROM cat_table WHERE url=:url")
    fun getCat(url: String): Cat

}