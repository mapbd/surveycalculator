package org.map_bd.surveycalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.map_bd.surveycalculator.databinding.ItemViewPagerBinding

class ViewPagerAdapter : ListAdapter<Int, ViewPagerAdapter.ImageViewHolder>(ImageDiffUtils()) {

    lateinit var binding : ItemViewPagerBinding

    inner class ImageViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(id : Int) {
            binding.imageView.setImageResource(id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_pager, parent, false)
        return ImageViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ImageDiffUtils : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
}