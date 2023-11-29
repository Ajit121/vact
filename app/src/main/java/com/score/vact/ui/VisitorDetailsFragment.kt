package com.score.vact.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.score.vact.R
import kotlinx.android.synthetic.main.visitor_details_fragment.*

class VisitorDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = VisitorDetailsFragment()
    }

    private lateinit var viewModel: VisitorDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visitor_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VisitorDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnConfirm.setOnClickListener {
         //   this.findNavController().navigate(R.id.action_visitorDetailsFragment_to_surveyFragment)
        }
    }
}
