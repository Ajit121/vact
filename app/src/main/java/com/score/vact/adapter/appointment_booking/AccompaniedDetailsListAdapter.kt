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
import com.score.vact.databinding.AccompaniedDetailsRowBinding
import com.score.vact.databinding.AccompaniedPersonRowBinding
import com.score.vact.model.appointment.AccompaniedBy
import com.score.vact.model.appointment_booking.AccompaniedPersonData
import com.score.vact.ui.common.DataBoundListAdapter
import com.score.vact.ui.common.DataBoundViewHolder

/**
 * A RecyclerView adapter for [Repo] class.
 */
class AccompaniedDetailsListAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<AccompaniedBy, AccompaniedDetailsRowBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AccompaniedBy>() {
        override fun areItemsTheSame(
            oldItem: AccompaniedBy,
            newItem: AccompaniedBy
        ): Boolean {
            return oldItem.number == newItem.number
                    && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: AccompaniedBy,
            newItem: AccompaniedBy
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): AccompaniedDetailsRowBinding {
        val binding = DataBindingUtil.inflate<AccompaniedDetailsRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.accompanied_details_row,
            parent,
            false
        )
       

        return binding
    }

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<AccompaniedDetailsRowBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        holder.binding.tvSlNo.text = "${position+1}"
    }

    override fun bind(binding: AccompaniedDetailsRowBinding, item: AccompaniedBy) {
        binding.person = item
    }
}
