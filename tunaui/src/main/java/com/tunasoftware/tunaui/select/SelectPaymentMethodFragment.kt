package com.tunasoftware.tunaui.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tunasoftware.tunaui.R
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
    

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(tuna_toolbar)

        val adapter = SelectPaymentMethodAdapter()
        adapter.setItems(paymentMethods())
        val recyclerView = rvPaymentMethods
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun paymentMethods(): MutableList<PaymentMethod> {
        return mutableListOf(
            PaymentMethod(12, "Cartão de crédito * * * * 5150"),
            PaymentMethod(0, "Novo cartão de crédito"),
            PaymentMethod(5, "Google pay", true),
            PaymentMethod(4, "Apple pay", true),
            PaymentMethod(3, "Pix", true),
            PaymentMethod(2, "Boleto", true)
        )
    }
}

