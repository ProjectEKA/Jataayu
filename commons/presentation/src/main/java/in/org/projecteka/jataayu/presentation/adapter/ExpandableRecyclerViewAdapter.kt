package `in`.org.projecteka.jataayu.presentation.adapter

import `in`.org.projecteka.jataayu.presentation.R
import `in`.org.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.IGroupDataBindingModel
import `in`.org.projecteka.jataayu.presentation.callback.ItemClickCallback
import `in`.org.projecteka.jataayu.presentation.ui.view.ExpandableLinearLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExpandableRecyclerViewAdapter(
    private val groupItemClickCallback: ItemClickCallback?,
    private val childItemClickCallback: ItemClickCallback,
    private val groupViewModelList: List<IGroupDataBindingModel>
) : GenericRecyclerViewAdapter(
    getItemClickCallback(
        groupItemClickCallback,
        null
    ), groupViewModelList
) {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerGroupViewHolder {
        val iGroupDataBindingModel: IGroupDataBindingModel = groupViewModelList[position]
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            iGroupDataBindingModel.layoutResId(),
            parent,
            false
        )
        val recyclerGroupViewHolder = RecyclerGroupViewHolder(binding, groupItemClickCallback)
        val itemView = recyclerGroupViewHolder.itemView
        if (!iGroupDataBindingModel.isExpanded) {
            collapseExpandableItem(itemView)
        }
        return recyclerGroupViewHolder
    }

    private fun collapseExpandableItem(itemView: View) {
        if (itemView is ExpandableLinearLayout) {
            itemView.changeState(false)
            val viewWithTag =
                itemView.findViewWithTag<View>(itemView.context.getString(R.string.body))
            viewWithTag.visibility = View.GONE
        }
    }

    inner class RecyclerGroupViewHolder(
        private val binding: ViewDataBinding,
        private val groupItemClickCallback: ItemClickCallback?
    ) : RecyclerViewHolder(binding) {
        override fun bind(iDataBindingModel: IDataBindingModel) {
            val iGroupDataBindingModel: IGroupDataBindingModel = iDataBindingModel as IGroupDataBindingModel
            binding.setVariable(iGroupDataBindingModel.dataBindingVariable(), iGroupDataBindingModel)
            binding.executePendingBindings()
            val recyclerView: RecyclerView =
                itemView.findViewById(iGroupDataBindingModel.childrenResourceId)
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
//            recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(
//                itemView.context,
//                R.anim.layout_animation_fall_down
//            )
            iGroupDataBindingModel.childrenViewModels?.let {
                recyclerView.adapter = GenericRecyclerViewAdapter(getItemClickCallback(childItemClickCallback, binding), it)
            }
            setItemClickListener(iGroupDataBindingModel, binding)
        }

        private fun setItemClickListener(
            IGroupDataBindingModel: IGroupDataBindingModel,
            viewDataBinding: ViewDataBinding
        ) {
            itemView.setOnClickListener { v: View ->
                (v as? ExpandableLinearLayout)?.toggleExpandedState()
                groupItemClickCallback?.onItemClick(IGroupDataBindingModel, viewDataBinding)
            }
        }

    }

    companion object {
        private fun getItemClickCallback(
            groupItemClickCallback: ItemClickCallback?,
            viewDataBinding: ViewDataBinding?
        ): ItemClickCallback {
            return object: ItemClickCallback {
                override fun onItemClick(iDataBindingModel: IDataBindingModel, itemViewBinding: ViewDataBinding) {
                    groupItemClickCallback?.onItemClick(iDataBindingModel, viewDataBinding!!)
                }
            }
        }
    }

}