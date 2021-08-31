package com.tunasoftware.tunaui.select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.widgets.TunaPaymentMethodWidget
import kotlinx.android.synthetic.main.model_payment_method.view.*

class SelectPaymentMethodAdapter : RecyclerView.Adapter<SelectPaymentMethodAdapter.ViewHolder>() {

    private val dataSet: MutableList<PaymentMethod> = mutableListOf()
    var current: PaymentMethod? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    private var _onClickListener: (PaymentMethod) -> Unit = {}
    private var _onRemoveItemListener: (PaymentMethod) -> Unit = {}

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val widget: TunaPaymentMethodWidget = view.findViewById(R.id.paymentMethodWidget)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.model_payment_method, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val paymentMethod = dataSet[position]
        viewHolder.apply {
            widget.apply {
                paymentMethodFlag = paymentMethod.methodFlag
                paymentMethodLabelSecondary = if (paymentMethod is PaymentMethodCreditCard) {
                    "${context.getString(R.string.paymet_method_credit_card_masked_number)} ${
                        paymentMethod.tunaUICard.maskedNumber.let {
                            it.trim().let { it.substring(it.length-4 until it.length) }
                        }
                    }"
                } else {
                    paymentMethod.displayName
                }
                paymentMethodSelected = current == paymentMethod
            }

            if (current != paymentMethod){
                view.tunaSwipeWidget.close()
            }

            view.tunaSwipeWidget.swipeDisabled = paymentMethod.disableSwipe

            view.tunaSwipeWidget.setOnItemClickListener {
                if (paymentMethod.selectable)
                    current = paymentMethod
                _onClickListener.invoke(paymentMethod)
                notifyDataSetChanged()
            }

            view.tunaSwipeWidget.setOnDeleteClickListener {
                _onRemoveItemListener.invoke(paymentMethod)
                removeAtPosition(dataSet.indexOf(paymentMethod))
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setOnClickListener(listener: (PaymentMethod) -> Unit) {
        this._onClickListener = listener
    }

    fun setOnRemoveItemListener(listener: (PaymentMethod) -> Unit) {
        this._onRemoveItemListener = listener
    }


    fun setItems(items: List<PaymentMethod>) {
        dataSet.clear()
        dataSet.addAll(items)
        current = items.firstOrNull { it.selectable }
        notifyDataSetChanged()
    }


    private fun removeAtPosition(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

}

