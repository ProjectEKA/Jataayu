package `in`.projecteka.jataayu.consent.ui.adapter

import `in`.projecteka.jataayu.consent.callback.DeleteConsentCallback
import `in`.projecteka.jataayu.core.databinding.ConsentItemBinding
import `in`.projecteka.jataayu.core.model.Consent
import `in`.projecteka.jataayu.presentation.adapter.GenericRecyclerViewAdapter
import `in`.projecteka.jataayu.presentation.callback.IDataBindingModel
import `in`.projecteka.jataayu.presentation.callback.ItemClickCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class ConsentsListAdapter(
    itemClickCallback: ItemClickCallback,
    requests: List<Consent>, val deleteConsentCallback: DeleteConsentCallback?
) : GenericRecyclerViewAdapter(requests, itemClickCallback) {

    constructor(itemClickCallback: ItemClickCallback,
                requests: List<Consent>): this(itemClickCallback, requests, null)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder {
        val iDataBindingModel: IDataBindingModel = listOfBindingModels[position]
        val binding: ViewDataBinding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                iDataBindingModel.layoutResId(),
                parent,
                false
            )
        return ConsentRecyclerViewHolder(binding)
    }

    inner class ConsentRecyclerViewHolder(
        private val binding: ViewDataBinding
    ) : GenericRecyclerViewAdapter.RecyclerViewHolder(binding) {

        override fun bind(iDataBindingModel: IDataBindingModel) {
            binding.setVariable(iDataBindingModel.dataBindingVariable(), iDataBindingModel)
            binding.executePendingBindings()
            setItemClickListener(iDataBindingModel)
            deleteConsentCallback?.let {
                (binding as ConsentItemBinding).ibDeleteConsent.setOnClickListener{
                    deleteConsentCallback.askForConsentPin(iDataBindingModel)
                }
            }
        }
    }
}