package com.threkcompany.cats.screens.cats

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.threkcompany.cats.R
import com.threkcompany.cats.databinding.FragmentCatsListBinding
import com.threkcompany.cats.logic.db.CatsDb
import com.threkcompany.cats.screens.cats.adapters.CatsAdapter

class CatsListFragment : Fragment() {
    lateinit var viewModel: CatsListViewModel
    lateinit var bind: FragmentCatsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_cats_list, container, false)

        setHasOptionsMenu(true)

        val recycler = bind.catsList
        val adapter = CatsAdapter(
            CatsAdapter.CatItemListener(
                { cat -> viewModel.clickOnCat(cat) },
                { position, size -> viewModel.bindPosition(position, size) })
        )
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(context, 2)

        val app = requireNotNull(this.activity).application
        val db = CatsDb.getInstance(app).catDbDao

        val factory = CatsListViewModelFactory(CatsListViewModel.Listener { this.showDialog() }, db)

        viewModel = ViewModelProvider(this, factory).get(CatsListViewModel::class.java)
        viewModel.cats.observe(viewLifecycleOwner, Observer { cats -> adapter.cats = cats })

        return bind.root
    }

    fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(R.string.save_cat_title)
        alertDialogBuilder.setNegativeButton(R.string.cancel) { _, _ -> viewModel.dialogResult(false) }
        alertDialogBuilder.setPositiveButton(R.string.apply) { _, _ -> viewModel.dialogResult(true) }
        alertDialogBuilder.create().show()
    }
}
