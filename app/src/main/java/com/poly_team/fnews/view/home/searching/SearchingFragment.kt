package com.poly_team.fnews.view.home.searching

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poly_team.fnews.R

class SearchingFragment : Fragment() {

    companion object {
        fun newInstance() = SearchingFragment()
    }

    private lateinit var viewModel: SearchingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_searching, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchingViewModel::class.java)
        // TODO: Use the ViewModel
    }

}