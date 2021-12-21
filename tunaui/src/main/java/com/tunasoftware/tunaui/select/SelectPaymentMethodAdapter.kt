package com.tunasoftware.tunaui.select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.widgets.TunaPaymentMethodWidget
import com.tunasoftware.tunaui.widgets.TunaSwipeWidget

class SelectPaymentMethodAdapter : RecyclerView.Adapter<SelectPaymentMethodAdapter.ViewHolder>() {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<PaymentMethod> = object : DiffUtil.ItemCallback<PaymentMethod>() {
            override fun areItemsTheSame(
                oldItem: PaymentMethod,
                newItem: PaymentMethod
            ): Boolean {
                if (oldItem is  PaymentMethodCreditCard && newItem is PaymentMethodCreditCard) {
                    return oldItem.tunaUICard.token == newItem.tunaUICard.token
                }
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PaymentMethod,
                newItem: PaymentMethod
            ): Boolean {
                return oldItem.displayName == newItem.displayName &&
                        oldItem.methodType == newItem.methodType &&
                        oldItem.disableSwipe == newItem.disableSwipe &&
                        oldItem.selectable == newItem.selectable &&
                        oldItem.selected == newItem.selected
            }
        }
    }

    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)
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
        val paymentMethod = mDiffer.currentList[position]
        viewHolder.apply {
            val tunaSwipeWidget = view.findViewById<TunaSwipeWidget>(R.id.tunaSwipeWidget)

            ViewCompat.addAccessibilityAction(view, view.context.getString(R.string.tuna_accessibility_select_item)) { _, _ ->
                onSelectPaymentMethod(paymentMethod)
                true
            }

            if (!paymentMethod.disableSwipe) {
                ViewCompat.addAccessibilityAction(view, view.context.getString(R.string.tuna_accessibility_remove_item)) { _, _ ->
                    onRemovePaymentMethod(paymentMethod)
                    true
                }
            }

            widget.apply {
                paymentMethodFlag = paymentMethod.methodFlag
                paymentMethodLabelSecondary = if (paymentMethod is PaymentMethodCreditCard) {
                    "${context.getString(R.string.payment_method_credit_card_masked_number)} ${
                        paymentMethod.tunaUICard.maskedNumber.let { number ->
                            number.trim().let { it.substring(it.length - 4 until it.length) }
                        }
                    }"
                } else {
                    when(paymentMethod.methodType){
                        PaymentMethodType.BANK_SLIP -> context.getString(R.string.bank_slip)
                        PaymentMethodType.NEW_CREDIT_CARD -> context.getString(R.string.new_credit_card)
                        else -> paymentMethod.displayName
                    }
                }
                paymentMethodSelected = paymentMethod.selected

                contentDescription = if (paymentMethodSelected == true) {
                    view.context.getString(R.string.tuna_accessibility_selected_card, paymentMethodLabelSecondary)
                } else {
                    paymentMethodLabelSecondary
                }
            }

            if (!paymentMethod.selected){
                tunaSwipeWidget.close()
            }

            tunaSwipeWidget.swipeDisabled = paymentMethod.disableSwipe

            tunaSwipeWidget.setOnItemClickListener { onSelectPaymentMethod(paymentMethod) }
            tunaSwipeWidget.setOnDeleteClickListener { onRemovePaymentMethod(paymentMethod) }
        }
    }

    override fun getItemCount() = mDiffer.currentList.size

    fun setOnClickListener(listener: (PaymentMethod) -> Unit) {
        this._onClickListener = listener
    }

    fun setOnRemoveItemListener(listener: (PaymentMethod) -> Unit) {
        this._onRemoveItemListener = listener
    }

    fun setItems(items: List<PaymentMethod>) {
        mDiffer.submitList(items)
    }

    fun getItemPosition(item: PaymentMethod?): Int {
        return mDiffer.currentList.indexOf(item);
    }

    private fun onRemovePaymentMethod(paymentMethod: PaymentMethod) {
        _onRemoveItemListener.invoke(paymentMethod)
    }

    private fun onSelectPaymentMethod(paymentMethod: PaymentMethod) {
        _onClickListener.invoke(paymentMethod)
    }

}

