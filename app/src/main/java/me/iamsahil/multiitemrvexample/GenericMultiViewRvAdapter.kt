package me.iamsahil.multiitemrvexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * A generic RecyclerView adapter supporting multiple item types with ViewBinding for flexibility.
 *
 * @param T The type of items to be displayed in the RecyclerView.
 * @param VB The type of ViewBinding associated with the item layout.
 * @property inflater A lambda function responsible for creating the ViewBinding object for each item.
 * @property viewHolderBinder A lambda function responsible for binding data to the ViewBinding object.
 * @property viewTypeProvider A lambda function to determine the view type for each item return An integer representing the view type of the item..
 * @param item The item for which the view type needs to be determined.
 * @property diffCallback The [DiffUtil.ItemCallback] used to calculate the difference between two lists of items.
 * @constructor Creates a GenericMultiRvAdapter with the specified parameters.
 */
class GenericMultiViewRvAdapter<T, VB : ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup, Boolean, Int) -> VB,
    private val viewHolderBinder: VB.(T) -> Unit,
    private val viewTypeProvider: ((item: T) -> Int)? = null,
    diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback()
) : ListAdapter<T, GenericMultiViewRvAdapter<T, VB>.ViewHolder>(diffCallback) {

    private var onItemClickListener: ((T) -> Unit)? = null

    /**
     * Sets the click listener for items in the RecyclerView.
     * @param listener The listener to be notified on item clicks with item as lambda param.
     */
    fun setOnItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypeProvider?.invoke(getItem(position)) ?: DEFAULT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = inflater.invoke(LayoutInflater.from(parent.context), parent, false, viewType)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.apply {
                viewHolderBinder.invoke(this, item)
                itemView.setOnClickListener {
                    onItemClickListener?.invoke(item)
                }
            }
        }
    }
    private class DefaultDiffCallback<T> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T & Any, newItem: T & Any) = oldItem == newItem
        override fun areContentsTheSame(oldItem: T & Any, newItem: T & Any) = true
    }
    companion object {
        private const val DEFAULT_VIEW_TYPE = 0
    }
}

