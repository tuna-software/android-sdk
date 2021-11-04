package com.tunasoftware.tunaui.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaPaymentMethodResultHandler
import com.tunasoftware.tunaui.TunaUIViewModelFactory
import com.tunasoftware.tunaui.creditcard.NewCardFragment
import com.tunasoftware.tunaui.databinding.SelectPaymentMethodFragmentBinding
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.domain.entities.flag
import com.tunasoftware.tunaui.extensions.getNavigationResult
import com.tunasoftware.tunaui.navigator
import com.tunasoftware.tunaui.utils.announceForAccessibility
import com.tunasoftware.tunaui.utils.setAsHeading

class SelectPaymentMethodFragment : Fragment() {

    lateinit var viewModel: SelectPaymentMethodViewModel

    private val adapter = SelectPaymentMethodAdapter()

    lateinit var binding : SelectPaymentMethodFragmentBinding

    lateinit var shimmerSelectPaymentMethod: ShimmerFrameLayout

    companion object {
        fun newInstance() = SelectPaymentMethodFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SelectPaymentMethodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, TunaUIViewModelFactory(requireActivity().application))[SelectPaymentMethodViewModel::class.java]
        binding.tunaToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_baseline_close_24)
            navigationContentDescription = getString(R.string.tuna_accessibility_close_button)
            setNavigationOnClickListener { activity?.finish() }
            setTitle(R.string.tuna_select_payment_method_title)
        }
        binding.btnSelect.setOnClickListener {
            viewModel.onSubmitClick()
        }

        binding.tvTitle.setAsHeading()
        shimmerSelectPaymentMethod = binding.root.findViewById(R.id.shimmer_select_payment_method)

        getNavigationResult<TunaUICard?>(R.id.selectPaymentMethodFragment, NewCardFragment.RESULT_CARD){
            it?.let {
                PaymentMethodCreditCard(PaymentMethodType.CREDIT_CARD, it.maskedNumber, flag = it.flag(), tunaUICard = it).run {
                    viewModel.onNewCardAdded(this)
                }
            }
        }

        adapter.setOnClickListener {
            if (it.methodType == PaymentMethodType.NEW_CREDIT_CARD){
                if (resources.getBoolean(R.bool.tuna_new_card_dialog_fragment_window_is_floating)) {
                    navigator.navigate(R.id.action_selectMethod_to_newCardDialog)
                } else {
                    navigator.navigate(R.id.action_selectMethod_to_newCard)
                }
            } else {
                viewModel.onPaymentMethodSelected(it)
                binding.rvPaymentMethods.postDelayed({
                    val holder = binding.rvPaymentMethods.findViewHolderForAdapterPosition(adapter.getItemPosition(it))
                    holder?.itemView?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
                }, 1000)
            }
        }
        adapter.setOnRemoveItemListener {
            if (it is PaymentMethodCreditCard)
                showDialogRemoveCard(it)
        }
        val recyclerView = binding.rvPaymentMethods
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        subscribe()
        viewModel.init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribe(){
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state){
                is UIState.Loading -> {
                    context?.announceForAccessibility(getString(R.string.tuna_accessibility_loading_list))
                    startShimmer()
                }
                is UIState.Success -> {
                    context?.announceForAccessibility(getString(R.string.tuna_accessibility_loaded_list))
                    stopShimmer()
                    adapter.setItems(state.result)
                }
                is UIState.Error -> {
                    //TODO
                    context?.announceForAccessibility(getString(R.string.tuna_accessibility_error_loading_list))
                    stopShimmer()
                }
            }
        })
        viewModel.actionsLiveData.observe(this , { action ->
            when (action){
                is ActionShowErrorDeletingCard -> {
                    Snackbar.make(binding.container, getString(R.string.error_removing_credit_card), Snackbar.LENGTH_SHORT).show()
                }
                is ActionOnPaymentMethodSelected -> {
                    requireActivity().let {
                        if (it is TunaPaymentMethodResultHandler){
                            it.onPaymentSelected(action.paymentMethod)
                        }
                    }
                }
            }
        })
    }

    private fun startShimmer() {
        binding.rvPaymentMethods.visibility = View.GONE
        binding.btnSelect.visibility = View.GONE
        shimmerSelectPaymentMethod.startShimmer()
    }

    private fun stopShimmer() {
        binding.rvPaymentMethods.visibility = View.VISIBLE
        binding.btnSelect.visibility = View.VISIBLE
        shimmerSelectPaymentMethod.stopShimmer()
        shimmerSelectPaymentMethod.visibility = View.GONE
    }

    private fun showDialogRemoveCard(paymentMethod: PaymentMethodCreditCard) {
        AlertDialog.Builder(requireContext(), R.style.Theme_TunaUI_Dialog_Alert)
            .setMessage(R.string.message_confirmation_remove_card)
            .setNegativeButton(R.string.button_label_no, null)
            .setPositiveButton(R.string.button_label_yes) { _, _ ->
                viewModel.onDeleteCard(paymentMethod, requireContext())
            }
            .create()
            .show()
    }

}

