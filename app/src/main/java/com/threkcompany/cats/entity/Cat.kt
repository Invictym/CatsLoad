package com.threkcompany.cats.entity

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Entity(tableName = "cat_table")
@TypeConverters(BreedsConverter::class)
data class Cat(
    var height: Int,
    @PrimaryKey var id: String,
    var url: String,
    var width: Int,
    var image: ByteArray,
    var breeds: List<Breed>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cat

        if (height != other.height) return false
        if (id != other.id) return false
        if (url != other.url) return false
        if (width != other.width) return false
        if (breeds != other.breeds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + id.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + width
        result = 31 * result + breeds.hashCode()
        return result
    }
}

class BreedsConverter {
    @TypeConverter
    fun fromBreeds(breeds: List<Breed>?): String? {
        return if (breeds == null) {
            Gson().toJson(breeds)
        } else {
            ""
        }
    }

    @TypeConverter
    fun toBreeds(breed: String): List<Breed> {
        if (breed == null) {
            return listOf()
        }
        var b = Gson().fromJson<List<Breed>>(breed, Breed::class.java)
        Log.w("EEEEEE", "$b $breed")
        return b ?: listOf()
    }

}