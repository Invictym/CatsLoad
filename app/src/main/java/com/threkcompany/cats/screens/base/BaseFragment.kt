package com.threkcompany.cats.screens.base

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(), BaseViewModel.MessageCallBack{

    override fun callBack(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}