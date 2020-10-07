package com.like.drive.carstory.ui.motor.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.databinding.HolderSelectMotorTypeItemBinding
import com.like.drive.carstory.ui.motor.viewmodel.MotorTypeViewModel

class SelectMotorTypeHolder(val binding: HolderSelectMotorTypeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: MotorTypeViewModel, item: MotorTypeData) {
        binding.vm = viewModel
        binding.motorData = item
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SelectMotorTypeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderSelectMotorTypeItemBinding.inflate(layoutInflater, parent, false)

            return SelectMotorTypeHolder(binding)
        }
    }
}