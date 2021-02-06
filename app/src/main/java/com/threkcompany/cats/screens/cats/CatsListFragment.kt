package com.threkcompany.cats.screens.cats

import android.Manifest
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.threkcompany.cats.R
import com.threkcompany.cats.databinding.FragmentCatsListBinding
import com.threkcompany.cats.entity.Cat
import com.threkcompany.cats.logic.db.CatsDb
import com.threkcompany.cats.screens.cats.adapters.CatsAdapter

class CatsListFragment : Fragment() {
    lateinit var viewModel: CatsListViewModel
    lateinit var bind: FragmentCatsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_cats_list, container, false)

        setHasOptionsMenu(true)

        val recycler = bind.catsList
        val adapter = CatsAdapter(
            CatsAdapter.CatItemListener(
                { cat, bitmap -> showDialog(cat, bitmap) },
                { position, size -> viewModel.bindPosition(position, size) })
        )
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(context, 2)

        val app = requireNotNull(this.activity).application
        val db = CatsDb.getInstance(app).catDbDao

        val factory = CatsListViewModelFactory(requireContext(), requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.path, db)

        viewModel = ViewModelProvider(this, factory).get(CatsListViewModel::class.java)
        viewModel.cats.observe(viewLifecycleOwner, Observer { cats -> adapter.cats = cats })

        ActivityCompat.requestPermissions(requireActivity(),
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),
            1)

        return bind.root
    }

    private fun showDialog(cat : Cat, bitmap: Bitmap) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(R.string.save_cat_title)
        alertDialogBuilder.setNegativeButton(R.string.cancel) { _, _ ->  }
        alertDialogBuilder.setNeutralButton(R.string.save_cat_to_storage) {_, _ -> viewModel.dialogResult(bitmap)}
        alertDialogBuilder.setPositiveButton(R.string.apply) { _, _ -> viewModel.dialogResult(cat) }
        alertDialogBuilder.create().show()
    }
}
