package `in`.projecteka.jataayu.presentation.adapter

import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

open class GenericRecyclerViewAdapter : RecyclerView.Adapter<GenericRecyclerViewAdapter.RecyclerViewHolder> {
    var listOfBindingModels = arrayListOf<IDataBindingModel>()
    var itemClickCallback: ItemClickCallback? = null

    constructor() {
        listOfBindingModels = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback) {
        this.itemClickCallback = itemClickCallback
        listOfBindingModels = ArrayList()
    }

    constructor(
        listIModels: List<IDataBindingModel>,
        itemClickCallback: ItemClickCallback
    ) {
        this.itemClickCallback = itemClickCallback
        listOfBindingModels.clear()
        listOfBindingModels.addAll(listIModels)
    }

    open fun updateData(bindingModels: List<IDataBindingModel>?) {
        bindingModels?.let {
            listOfBindingModels.clear()
            listOfBindingModels.addAll(bindingModels)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder {
        val iDataBindingModel: IDataBindingModel = listOfBindingModels[position]
        val binding: ViewDataBinding =
            inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                iDataBindingModel.layoutResId(),
                parent,
                false
            )
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewModel = listOfBindingModels[position]
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return listOfBindingModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    open inner class RecyclerViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(iDataBindingModel: IDataBindingModel) {
            binding.setVariable(iDataBindingModel.dataBindingVariable(), iDataBindingModel)
            binding.executePendingBindings()
            setItemClickListener(iDataBindingModel)
        }

        open fun setItemClickListener(iDataBindingModel: IDataBindingModel) {
            itemView.setOnClickListener {
                itemClickCallback?.onItemClick(iDataBindingModel, binding)
            }
        }
    }

    fun addItem(iDataBindingModel: IDataBindingModel): Int {
        listOfBindingModels.add(iDataBindingModel)
        return listOfBindingModels.size
    }

    fun removeItem(iDataBindingModel: IDataBindingModel): Int {
        val index = listOfBindingModels.indexOf(iDataBindingModel)
        listOfBindingModels.remove(iDataBindingModel)
        return index
    }
}
