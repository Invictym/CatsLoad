package com.threkcompany.cats.logic.db

import android.content.Context
import androidx.room.*
import com.threkcompany.cats.entity.BreedsConverter
import com.threkcompany.cats.entity.Cat

@Database(entities = [Cat::class], version = 1, exportSchema = false)
@TypeConverters(BreedsConverter::class)
abstract class CatsDb : RoomDatabase() {

    abstract val catDbDao: CatsDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: CatsDb? = null

        fun getInstance(context: Context): CatsDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CatsDb::class.java,
                        "cats_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance!!
            }
        }
    }
}