package com.tunasoftware.tunaui.delivery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.extensions.dp

class SelectDeliveryAdapter : RecyclerView.Adapter<SelectDeliveryAdapter.ViewHolder>() {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Delivery> =
            object : DiffUtil.ItemCallback<Delivery>() {
                override fun areItemsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: Delivery, newItem: Delivery): Boolean {
                    return oldItem.id == newItem.id &&
                            oldItem.name == newItem.name &&
                            oldItem.description == newItem.description &&
                            oldItem.value == newItem.value &&
                            oldItem.selected == newItem.selected
                }
            }
    }

    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)
    private var _onClickListener: (Delivery) -> Unit = {}

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rbSelected: RadioButton = view.findViewById(R.id.rbSelected)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvValue: TextView = view.findViewById(R.id.tvValue)
        val content: CardView = view.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.model_select_delivery, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val delivery = mDiffer.currentList[position]
        viewHolder.apply {
            ViewCompat.addAccessibilityAction(view, view.context.getString(R.string.tuna_accessibility_select_item)) { _, _ ->
                onSelectDelivery(delivery)
                true
            }

            rbSelected.isChecked = delivery.selected
            tvName.text = delivery.name
            tvDescription.text = delivery.description
            tvValue.text = delivery.value

            if (delivery.selected) {
                content.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.tunaui_primary_color))
                content.cardElevation = 2.dp.toFloat()
            } else {
                content.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.tuna_background))
                content.cardElevation = 0f
            }

            rbSelected.setOnClickListener { onSelectDelivery(delivery) }
            view.setOnClickListener { onSelectDelivery(delivery) }
        }
    }

    override fun getItemCount() = mDiffer.currentList.size

    fun setOnClickListener(listener: (Delivery) -> Unit) {
        this._onClickListener = listener
    }

    fun setItems(items: List<Delivery>) {
        mDiffer.submitList(items)
    }

    fun getItemPosition(item: Delivery?): Int {
        return mDiffer.currentList.indexOf(item);
    }

    private fun onSelectDelivery(delivery: Delivery) {
        _onClickListener.invoke(delivery)
    }

}

