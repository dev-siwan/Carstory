package com.like.drive.carstory.ui.motor.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.motor.holder.SelectMotorTypeHolder
import com.like.drive.carstory.ui.motor.viewmodel.MotorTypeViewModel

class SelectMotorTypeAdapter(val viewModel:MotorTypeViewModel) : ListAdapter<MotorTypeData, SelectMotorTypeHolder>(
    object :DiffUtil.ItemCallback<MotorTypeData>(){
        override fun areItemsTheSame(oldItem: MotorTypeData, newItem: MotorTypeData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MotorTypeData, newItem: MotorTypeData): Boolean {
            return "${oldItem.brandCode}${oldItem.modelCode}" == "${newItem.brandCode}${newItem.modelCode}"
        }
    }
) {

    var motorTypeList = listOf<MotorTypeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMotorTypeHolder {
        return SelectMotorTypeHolder.from(parent)
    }

    override fun getItemCount() = motorTypeList.size

    override fun onBindViewHolder(holder: SelectMotorTypeHolder, position: Int) {
        holder.bind(viewModel,motorTypeList[position])
    }


}