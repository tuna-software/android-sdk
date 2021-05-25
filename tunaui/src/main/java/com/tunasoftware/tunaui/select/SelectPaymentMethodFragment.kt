package com.tunasoftware.tunaui.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.creditcard.NewCardFragment
import com.tunasoftware.tunaui.domain.entities.TunaUICard
import com.tunasoftware.tunaui.extensions.getNavigationResult
import com.tunasoftware.tunaui.navigator
import kotlinx.android.synthetic.main.select_payment_method_fragment.*


class SelectPaymentMethodFragment : Fragment() {

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
        tuna_toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_baseline_close_24)
            setNavigationOnClickListener(View.OnClickListener {
                activity?.finish()
            })
            setTitle(R.string.tuna_select_payment_method_title)
        }


        val adapter = SelectPaymentMethodAdapter()
        val new =  getNavigationResult<TunaUICard>(NewCardFragment.RESULT_CARD)?.let {
             PaymentMethod(12, "Cartão de crédito ${it.maskedNumber}")
        }
        adapter.setItems(
            new?.let { (listOf(new) + paymentMethods()).toMutableList() }?:paymentMethods()
        )
        new?.let {
            adapter.current = it
        }
        adapter.setOnClickListener {
            if (it.methodType == 0){
                navigator.navigate(R.id.action_selectMethod_to_newCard)
            }
        }
        val recyclerView = rvPaymentMethods
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun paymentMethods(): MutableList<PaymentMethod> {
        return mutableListOf(
            PaymentMethod(12, "Cartão de crédito * * * * 5150"),
            PaymentMethod(12, "Cartão de crédito * * * * 5151"),
            PaymentMethod(0, "Novo cartão de crédito",true, selectable = false),
            PaymentMethod(5, "Google pay", true, selectable = false),
            PaymentMethod(3, "Pix", true, selectable = false),
            PaymentMethod(2, "Boleto", true, selectable = false)
        )
    }
}

