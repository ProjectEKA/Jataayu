package `in`.org.projecteka.jataayu.presentation.adapter

import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

open class GenericRecyclerViewAdapter : RecyclerView.Adapter<GenericRecyclerViewAdapter.RecyclerViewHolder> {
    var listOfBindingModels: List<IDataBindingModel>? = null
    var itemClickCallback: ItemClickCallback? = null

    constructor() {
        this.listOfBindingModels = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback) {
        this.itemClickCallback = itemClickCallback
        this.listOfBindingModels = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback, listIModel: List<IDataBindingModel>) {
        this.itemClickCallback = itemClickCallback
        this.listOfBindingModels = listIModel
    }

    open fun updateData(bindingModels: List<IDataBindingModel>?) {
        listOfBindingModels = bindingModels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder {
        val iDataBindingModel: IDataBindingModel = listOfBindingModels!![position]
        val binding: ViewDataBinding =
            inflate<ViewDataBinding>(LayoutInflater.from(parent.context), iDataBindingModel.layoutResId(), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewModel = listOfBindingModels!![position]
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return if (listOfBindingModels == null) 0 else listOfBindingModels!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    open inner class RecyclerViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(iModel: IDataBindingModel) {
            binding.setVariable(iModel.dataBindingVariable(), iModel)
            binding.executePendingBindings()
            setItemClickListener(iModel)
        }

        open fun setItemClickListener(iDataBindingModel: IDataBindingModel) {
            itemView.setOnClickListener {
                itemClickCallback?.onItemClick(iDataBindingModel, binding)
            }
        }
    }
}
