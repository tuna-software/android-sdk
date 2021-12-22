package com.tunasoftware.tunaui.installment

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

class SelectInstallmentAdapter : RecyclerView.Adapter<SelectInstallmentAdapter.ViewHolder>() {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Installment> =
            object : DiffUtil.ItemCallback<Installment>() {
                override fun areItemsTheSame(oldItem: Installment, newItem: Installment): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: Installment, newItem: Installment): Boolean {
                    return oldItem.id == newItem.id &&
                            oldItem.name == newItem.name &&
                            oldItem.description == newItem.description &&
                            oldItem.selected == newItem.selected
                }
            }
    }

    private val mDiffer = AsyncListDiffer(this, DIFF_CALLBACK)
    private var _onClickListener: (Installment) -> Unit = {}

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rbSelected: RadioButton = view.findViewById(R.id.rbSelected)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val content: CardView = view.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.model_select_installment, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val installment = mDiffer.currentList[position]
        viewHolder.apply {
            ViewCompat.addAccessibilityAction(view, view.context.getString(R.string.tuna_accessibility_select_item)) { _, _ ->
                onSelectInstallment(installment)
                true
            }

            rbSelected.isChecked = installment.selected
            tvName.text = installment.name
            tvDescription.text = installment.description

            if (installment.selected) {
                content.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.tunaui_primary_color))
                content.cardElevation = 2.dp.toFloat()
            } else {
                content.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.tuna_background))
                content.cardElevation = 0f
            }

            rbSelected.setOnClickListener { onSelectInstallment(installment) }
            view.setOnClickListener { onSelectInstallment(installment) }
        }
    }

    override fun getItemCount() = mDiffer.currentList.size

    fun setOnClickListener(listener: (Installment) -> Unit) {
        this._onClickListener = listener
    }

    fun setItems(items: List<Installment>) {
        mDiffer.submitList(items)
    }

    fun getItemPosition(item: Installment?): Int {
        return mDiffer.currentList.indexOf(item);
    }

    private fun onSelectInstallment(installment: Installment) {
        _onClickListener.invoke(installment)
    }

}

