package com.threkcompany.cats.screens.savedcats

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.threkcompany.cats.R
import com.threkcompany.cats.databinding.FragmentCatsListBinding
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDb
import com.threkcompany.cats.screens.base.BaseFragment
import com.threkcompany.cats.screens.cats.adapters.CatsAdapter

class SavedCatsFragment : BaseFragment() {

    private lateinit var viewModel: SavedCatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = DataBindingUtil.inflate<FragmentCatsListBinding>(
            inflater,
            R.layout.fragment_cats_list,
            container,
            false
        )

        val app = requireNotNull(this.activity).application
        val db = CatsDb.getInstance(app).catDbDao
        val factory = SavedCatsViewModelFactory(requireContext(), requireContext().getExternalFilesDir(
            Environment.DIRECTORY_DOWNLOADS)!!.path, db, this)
        viewModel = ViewModelProvider(this, factory).get(SavedCatsViewModel::class.java)

        val recyclerView = bind.catsList
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val adapter = CatsAdapter(
            CatsAdapter.CatItemListener({ cat, bitmap -> showDialog(cat, bitmap) },
                { _, _ -> }))
        recyclerView.adapter = adapter

        viewModel.cats.observe(viewLifecycleOwner, Observer { cats ->
            adapter.cats = cats
        })
        return bind.root
    }

    private fun showDialog(cat: Cat, bitmap: Bitmap) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(R.string.delete_cats)
        alertDialogBuilder.setNegativeButton(R.string.cancel) { _, _ ->  }
        alertDialogBuilder.setNeutralButton(R.string.save_cat_to_storage) {_, _ -> viewModel.dialogResult(bitmap)}
        alertDialogBuilder.setPositiveButton(R.string.apply) { _, _ -> viewModel.dialogResultRemove(cat) }
        alertDialogBuilder.create().show()
    }
}
