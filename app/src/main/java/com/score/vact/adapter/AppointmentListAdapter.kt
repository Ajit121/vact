package com.score.vact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.score.vact.AppExecutors
import com.score.vact.R
import com.score.vact.databinding.AppointmentsRowBinding
import com.score.vact.model.appointment.AppointmentData
import com.score.vact.ui.appointment_list.Action
import com.score.vact.ui.common.DataBoundListAdapter

class AppointmentListAdapter(
    val appExecutors: AppExecutors,
    private val onClick: ((AppointmentData) -> Unit)?,
    private val onActionClick:((Int,Action) ->Unit)?
) :
    DataBoundListAdapter<AppointmentData, AppointmentsRowBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<AppointmentData>() {
            override fun areItemsTheSame(
                oldItem: AppointmentData,
                newItem: AppointmentData
            ): Boolean {
                return oldItem.id == newItem.id || oldItem.status == newItem.status
            }

            override fun areContentsTheSame(
                oldItem: AppointmentData,
                newItem: AppointmentData
            ): Boolean {
                return oldItem == newItem
            }

        }
    ) {
    override fun createBinding(parent: ViewGroup): AppointmentsRowBinding {
        val binding =
            DataBindingUtil.inflate<AppointmentsRowBinding>(
                LayoutInflater.from(parent.context),
                R.layout.appointments_row,
                parent,
                false
            )
        binding.root.setOnClickListener {
            binding.appointment?.let {
                onClick?.invoke(it)
            }
        }
        binding.btnReject.setOnClickListener{
            binding.appointment?.let {
                onActionClick?.invoke(it.id,Action.REJECT)
            }
        }
        binding.btnModify.setOnClickListener{
            binding.appointment?.let {
                onActionClick?.invoke(it.id,Action.MODIFY)
            }
        }
        binding.btnApprove.setOnClickListener{
            binding.appointment?.let {
                onActionClick?.invoke(it.id,Action.APPROVE)
            }
        }
        return binding
    }

    override fun bind(binding: AppointmentsRowBinding, item: AppointmentData) {
        binding.appointment = item
    }
}