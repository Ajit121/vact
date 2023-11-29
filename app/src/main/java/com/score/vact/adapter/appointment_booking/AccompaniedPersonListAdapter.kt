/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.score.vact.adapter.appointment_booking

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.score.vact.AppExecutors
import com.score.vact.R
import com.score.vact.databinding.AccompaniedPersonRowBinding
import com.score.vact.model.appointment_booking.AccompaniedPersonData
import com.score.vact.ui.common.DataBoundListAdapter

/**
 * A RecyclerView adapter for [Repo] class.
 */
class AccompaniedPersonListAdapter(
    appExecutors: AppExecutors,
    private val removeCallback: ((AccompaniedPersonData) -> Unit)?
) : DataBoundListAdapter<AccompaniedPersonData, AccompaniedPersonRowBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AccompaniedPersonData>() {
        override fun areItemsTheSame(
            oldItem: AccompaniedPersonData,
            newItem: AccompaniedPersonData
        ): Boolean {
            return oldItem.number == newItem.number
                    && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: AccompaniedPersonData,
            newItem: AccompaniedPersonData
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AccompaniedPersonRowBinding {
        val binding = DataBindingUtil.inflate<AccompaniedPersonRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.accompanied_person_row,
            parent,
            false
        )
       
        binding.imgDelete.setOnClickListener {
            binding.person?.let {
                removeCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: AccompaniedPersonRowBinding, item: AccompaniedPersonData) {
        binding.person = item
    }
}
