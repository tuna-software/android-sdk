package com.tunasoftware.tunaui.creditcard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tunasoftware.tunaui.R

class NewCardDialogFragment : NewCardFragment() {

    companion object {
        fun newInstance() = NewCardDialogFragment()
    }

    override fun onStart() {
        super.onStart()
        resources.getBoolean(R.bool.tuna_new_card_dialog_fragment_window_is_floating).let {
            if (it) {
                val width = resources.getDimension(R.dimen.tuna_select_payment_method_layout_width)
                val height = resources.getDimension(R.dimen.tuna_select_payment_method_layout_height)
                dialog?.window?.setLayout(width.toInt(), height.toInt())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_TunaUI_DialogFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        arguments?.let { NewCardDialogFragmentArgs.fromBundle(it) }?.let {
            showCpfField = it.shouldShowCpf
        }
        return view
    }

    override fun getNavigationIconDrawable(): Drawable? {
        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_close)
        if (icon != null) {
            DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.tunaui_primary_dark_color))
        }
        return ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_close)
    }

    override fun getCurrentFocus(): View? {
        return dialog?.currentFocus
    }
}