package com.score.vact.adapter.appointment_booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.score.vact.AppExecutors
import com.score.vact.R
import com.score.vact.databinding.BelongingsRowBinding
import com.score.vact.model.appointment_booking.AccompaniedPersonData
import com.score.vact.model.appointment_booking.BelongingsData
import com.score.vact.ui.common.DataBoundListAdapter

class BelongingsAdapter(
    appExecutors: AppExecutors,
    private val onCheckCallback: ((BelongingsData) -> Unit)?
) : DataBoundListAdapter<BelongingsData, BelongingsRowBinding>(
        appExecutors = appExecutors,
        diffCallback = object :  DiffUtil.ItemCallback<BelongingsData>() {
            override fun areItemsTheSame(
                oldItem: BelongingsData,
                newItem: BelongingsData
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.isSelected == newItem.isSelected
            }

            override fun areContentsTheSame(
                oldItem: BelongingsData,
                newItem: BelongingsData
            ): Boolean {
                return oldItem == newItem
            }
        }) {
    override fun createBinding(parent: ViewGroup): BelongingsRowBinding {

        val binding = DataBindingUtil.inflate<BelongingsRowBinding>(
            LayoutInflater.from(parent.context),
            R.layout.belongings_row,
            parent,
            false
        )
        binding.checkbox.setOnCheckedChangeListener { _, b ->
            binding.belongingItem?.let {
               val updatedItem = it.copy(isSelected = b)
                onCheckCallback?.invoke(updatedItem)
            }
        }
        return binding
    }

    override fun bind(binding: BelongingsRowBinding, item: BelongingsData) {
        binding.belongingItem = item
    }
}