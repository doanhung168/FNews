package com.poly_team.fnews.view.home

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.poly_team.fnews.R

abstract class HomeBaseFragment<B : ViewDataBinding> : Fragment() {

    protected var mBinding: B? = null
    protected lateinit var mNavController: NavController

    abstract fun getLayout(): Int
    abstract fun setInsets(left: Int, top: Int, right: Int, bottom: Int)

    @SuppressLint("ClickableViewAccessibility")
    protected fun disableScreen() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    protected fun enableScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!mNavController.popBackStack()) {
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(requireActivity(), R.id.home_nav_host)

        ViewCompat.setOnApplyWindowInsetsListener(
            requireActivity().window.decorView
        ) { _: View?, insets: WindowInsetsCompat ->
            val insetsValue = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            setInsets(
                insetsValue.left,
                insetsValue.top,
                insetsValue.right,
                insetsValue.bottom
            )
            insets
        }

    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}