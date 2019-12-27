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
    var listOfBindingModel: List<IDataBindingModel>? = null
    var itemClickCallback: ItemClickCallback? = null

    constructor() {
        this.listOfBindingModel = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback) {
        this.itemClickCallback = itemClickCallback
        this.listOfBindingModel = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback, listIModel: List<IDataBindingModel>) {
        this.itemClickCallback = itemClickCallback
        this.listOfBindingModel = listIModel
    }

    open fun updateData(bindingModels: List<IDataBindingModel>?) {
        listOfBindingModel = bindingModels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder {
        val iDataBindingModel: IDataBindingModel = listOfBindingModel!![position]
        val binding: ViewDataBinding =
            inflate<ViewDataBinding>(LayoutInflater.from(parent.context), iDataBindingModel.layoutResId(), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewModel = listOfBindingModel!![position]
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return if (listOfBindingModel == null) 0 else listOfBindingModel!!.size
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
