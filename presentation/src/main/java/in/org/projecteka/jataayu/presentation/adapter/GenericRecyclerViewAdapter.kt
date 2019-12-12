package `in`.org.projecteka.jataayu.presentation.adapter

import `in`.org.projecteka.jataayu.presentation.callback.IDataBinding
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

open class GenericRecyclerViewAdapter : RecyclerView.Adapter<GenericRecyclerViewAdapter.RecyclerViewHolder> {
    var listOfBinding: List<IDataBinding>? = null
    var itemClickCallback: ItemClickCallback? = null

    constructor() {
        this.listOfBinding = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback) {
        this.itemClickCallback = itemClickCallback
        this.listOfBinding = ArrayList()
    }

    constructor(itemClickCallback: ItemClickCallback, listI: List<IDataBinding>) {
        this.itemClickCallback = itemClickCallback
        this.listOfBinding = listI
    }

    open fun updateData(bindings: List<IDataBinding>?) {
        listOfBinding = bindings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder {
        val iDataBinding: IDataBinding = listOfBinding!![position]
        val binding: ViewDataBinding =
            inflate<ViewDataBinding>(LayoutInflater.from(parent.context), iDataBinding.layoutResId(), parent, false)
        return RecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewModel = listOfBinding!![position]
        holder.bind(viewModel)
    }

    override fun getItemCount(): Int {
        return if (listOfBinding == null) 0 else listOfBinding!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    open inner class RecyclerViewHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(i: IDataBinding) {
            binding.setVariable(i.dataBindingVariable(), i)
            binding.executePendingBindings()
            setItemClickListener(i)
        }

        open fun setItemClickListener(i: IDataBinding) {
            itemView.setOnClickListener {
                itemClickCallback?.performItemClickAction(i, binding)
            }
        }
    }
}
