package com.mikirinkode.openstreetmapsample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikirinkode.openstreetmapsample.databinding.ItemSearchResultBinding

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder>() {

    private val list = ArrayList<SearchResultResponse>()

    var onItemClick: ((SearchResultResponse) -> Unit)? = null

    fun setList(newList: List<SearchResultResponse>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(response: SearchResultResponse){
                binding.apply {
                    tvLocationName.text = response.displayName
                }
            }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(list[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(list[position])
    }
}