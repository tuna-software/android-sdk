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
    private var _current: PaymentMethod? = null
    private var _onClickListener: (PaymentMethod) -> Unit = {}

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
                paymentMethodFlag = paymentMethod.flag
                paymentMethodLabelSecondary = paymentMethod.displayName
                paymentMethodSelected = _current == paymentMethod
            }

            if (_current != paymentMethod){
                view.tunaSwipeWidget.close()
            }

            view.tunaSwipeWidget.swipeDisabled = paymentMethod.disableSwipe

            view.tunaSwipeWidget.setOnItemClickListener {
                _current = paymentMethod
                _onClickListener.invoke(paymentMethod)
                notifyDataSetChanged()
            }

            view.tunaSwipeWidget.setOnDeleteClickListener {
                removeAtPosition(position)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun setOnClickListener(listener: (PaymentMethod) -> Unit) {
        this._onClickListener = listener
    }

    fun setItems(items: MutableList<PaymentMethod>) {
        dataSet.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAtPosition(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
    }

}

