package com.threkcompany.cats.screens.cats.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.threkcompany.cats.R
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.enternet.LoadImagePicasso
import java.io.ByteArrayOutputStream


class CatsAdapter(val listener: CatItemListener) : RecyclerView.Adapter<CatsAdapter.ViewHolder>() {

    var cats = listOf<Cat>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return cats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cats[position], listener, position, itemCount)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var catImage: ImageView = itemView.findViewById(R.id.cat_item)

        fun bind(item: Cat, listener: CatItemListener, position: Int, size: Int) {
            itemView.setOnClickListener {
                val d = catImage.drawable as? BitmapDrawable ?: return@setOnClickListener
                val bitmap = d.bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                item.image = stream.toByteArray()
                listener.onClick(item)
            }

            listener.bindPosition(position, size)

            if (item.image != null && item.image.size > 2) {
                val bmp = BitmapFactory.decodeByteArray(item.image, 0, item.image.size)
                catImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        bmp, 700, 700,
                        false
                    )
                )
            } else {
                LoadImagePicasso().load(catImage, getPlaceHolder(itemView.context.applicationContext), item.url)
            }
        }

        private fun getPlaceHolder(context: Context): CircularProgressDrawable {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            return circularProgressDrawable
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.cat_layout, parent, false)
                val params = view.layoutParams
                params.height = parent.context.resources.displayMetrics.heightPixels / 3
                view.layoutParams = params
                return ViewHolder(view)
            }
        }
    }



    class CatItemListener(var function: (cat: Cat) -> Unit, var funPosition: (position: Int, size: Int) -> Unit) {
        fun onClick(cat: Cat) = function(cat)
        fun bindPosition(position: Int, size: Int) = funPosition(position, size)
    }
}