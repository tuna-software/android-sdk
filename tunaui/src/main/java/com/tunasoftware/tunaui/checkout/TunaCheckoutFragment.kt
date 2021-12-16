package com.tunasoftware.tunaui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.CheckoutFragmentBinding

class TunaCheckoutFragment : Fragment() {

    lateinit var binding: CheckoutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CheckoutFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tunaToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_arrow_back)
            navigationContentDescription = getString(R.string.tuna_accessibility_close_button)
            setNavigationOnClickListener { activity?.finish() }
            setTitle(R.string.tuna_checkout_title)
        }

    }
}