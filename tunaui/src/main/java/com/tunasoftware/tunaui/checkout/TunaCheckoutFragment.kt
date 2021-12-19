package com.tunasoftware.tunaui.checkout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tunasoftware.android.Tuna
import com.tunasoftware.android.kt.getSandboxSessionId
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUI
import com.tunasoftware.tunaui.databinding.CheckoutFragmentBinding
import com.tunasoftware.tunaui.domain.entities.PaymentMethodSelectionResult
import com.tunasoftware.tunaui.domain.entities.TunaCardFlag
import com.tunasoftware.tunaui.domain.entities.cardFlag
import com.tunasoftware.tunaui.widgets.State

class TunaCheckoutFragment : Fragment() {

    lateinit var binding: CheckoutFragmentBinding
    lateinit var tunaUI : TunaUI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CheckoutFragmentBinding.inflate(inflater, container, false)
        tunaUI = TunaUI(requireActivity() as AppCompatActivity)
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

        binding.checkoutPaymentMethod.setOnClickListener {
            Tuna.getSandboxSessionId()
                    .onSuccess {
                        Tuna.startSession(it)
                        tunaUI.selectPaymentMethod(object : TunaUI.TunaSelectPaymentMethodCallback {
                            override fun onPaymentMethodSelected(paymentMethodSelectionResult: PaymentMethodSelectionResult) {
                                paymentMethodSelectionResult.cardInfo?.let { card ->
                                    TunaCardFlag.fromBrand(card.brand).let { flag ->
                                        binding.checkoutPaymentMethod.checkoutFlag = ContextCompat.getDrawable(requireContext(), flag.cardFlag)
                                    }
                                    binding.checkoutPaymentMethod.checkoutLabelPrimary = card.maskedNumber
                                }
                            }

                            override fun onCancelled() {
                                Log.i("TunaCheckoutFragment", "cancelled")
                            }
                        })
                    }
                    .onFailure {
                        Log.e("TunaCheckoutFragment", "Error start session", it)
                    }
        }

        var success = false
        binding.checkoutPromoCode.setOnRedeemListener {
            binding.checkoutPromoCode.setState(State.Loading(true))
            Handler(Looper.getMainLooper()).postDelayed({
                binding.checkoutPromoCode.setState(State.Loading(false))
                if (success) {
                    binding.checkoutPromoCode.setState(State.Success("- R$ 43,25"))
                } else {
                    binding.checkoutPromoCode.setState(State.Error("Invalid code", "This promo code expired at 31/08/2021"))
                }
                success = !success
            }, 3000)
        }

        binding.checkoutPromoCode.setOnRemovedListener {
            Log.d("TunaCheckoutFragment", "Promo code removed...")
        }
    }
}