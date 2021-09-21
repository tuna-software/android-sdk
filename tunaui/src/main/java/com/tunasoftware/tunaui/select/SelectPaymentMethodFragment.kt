package com.tunasoftware.tunaui.select

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUIViewModelFactory
import com.tunasoftware.tunaui.creditcard.NewCardFragment
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.domain.entities.flag
import com.tunasoftware.tunaui.extensions.getNavigationResult
import com.tunasoftware.tunaui.navigator
import com.tunasoftware.tunaui.utils.announceForAccessibility
import com.tunasoftware.tunaui.utils.setAsHeading
import kotlinx.android.synthetic.main.select_payment_method_fragment.*
import kotlinx.android.synthetic.main.select_payment_method_shimmer.*

class SelectPaymentMethodFragment : Fragment() {

    lateinit var viewModel: SelectPaymentMethodViewModel

    private val adapter = SelectPaymentMethodAdapter()

    companion object {
        fun newInstance() = SelectPaymentMethodFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_payment_method_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, TunaUIViewModelFactory(requireActivity().application))[SelectPaymentMethodViewModel::class.java]
        tuna_toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_baseline_close_24)
            navigationContentDescription = getString(R.string.tuna_accessibility_close_button)
            setNavigationOnClickListener { activity?.finish() }
            setTitle(R.string.tuna_select_payment_method_title)
        }

        tvTitle.setAsHeading()

        val new =  getNavigationResult<TunaUICard>(NewCardFragment.RESULT_CARD)?.let {
             PaymentMethodCreditCard(PaymentMethodType.CREDIT_CARD, it.maskedNumber, flag = it.flag(), tunaUICard = it)
        }
        new?.let {
            adapter.current = it
        }
        adapter.setOnClickListener {
            if (it.methodType == PaymentMethodType.NEW_CREDIT_CARD){
                navigator.navigate(R.id.action_selectMethod_to_newCard)
            } else {
                rvPaymentMethods.postDelayed({
                    val holder = rvPaymentMethods.findViewHolderForAdapterPosition(adapter.getItemPosition(it))
                    holder?.itemView?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
                }, 1000)
            }
        }
        adapter.setOnRemoveItemListener {
            if (it is PaymentMethodCreditCard)
                viewModel.onDeleteCard(it, context)
        }
        val recyclerView = rvPaymentMethods
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
                    Snackbar.make(container, getString(R.string.error_removing_credit_card), Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun startShimmer() {
        rvPaymentMethods.visibility = View.GONE
        btnSelect.visibility = View.GONE
        shimmer_select_payment_method.startShimmer()
    }

    private fun stopShimmer() {
        rvPaymentMethods.visibility = View.VISIBLE
        btnSelect.visibility = View.VISIBLE
        shimmer_select_payment_method.stopShimmer()
        shimmer_select_payment_method.visibility = View.GONE
    }

}

