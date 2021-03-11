package com.tunasoftware.tunaui.creditcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.NewCardFragmentBinding
import com.tunasoftware.tunaui.extensions.displayMetrics
import com.tunasoftware.tunaui.extensions.layout
import com.tunasoftware.tunaui.extensions.px
import com.tunasoftware.tunaui.extensions.textLayoutParent
import com.tunasoftware.tunaui.utils.Mask
import kotlinx.android.synthetic.main.new_card_fragment.*

class NewCardFragment : Fragment() {

    companion object {
        fun newInstance() = NewCardFragment()
    }

    private lateinit var viewModel: NewCardViewModel
    private lateinit var binding: NewCardFragmentBinding
    private lateinit var listOfFields : List<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, ).get(NewCardViewModel::class.java)
        binding = NewCardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(tuna_toolbar)
        setupFields()
    }

    private fun setupFields(){
        val fieldWidth = requireContext().displayMetrics.widthPixels - 32.px
        listOfFields = listOf(binding.etNumber, binding.etName, binding.etExDate, binding.etCvv, binding.etCpf, binding.etPhone)

        binding.etNumber.layout(width = fieldWidth)
        binding.etName.layout(width = fieldWidth)
        binding.etExDate.layout(width = fieldWidth)
        binding.etCvv.layout(width = fieldWidth)
        binding.etCpf.layout(width = fieldWidth)
        binding.etPhone.layout(width = fieldWidth)

        val focusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.vpCardFields.smoothScrollTo(view.textLayoutParent().x.toInt(), 0)
                listOfFields.forEach {
                    if (it != view){
                        it.setOnTouchListener { view, motionEvent ->  true}
                    } else {
                        it.setOnTouchListener { view, motionEvent ->  false}
                    }
                }
            }

        }
        binding.etNumber.onFocusChangeListener = focusListener
        binding.etName.onFocusChangeListener = focusListener
        binding.etExDate.onFocusChangeListener = focusListener
        binding.etCvv.onFocusChangeListener = focusListener
        binding.etCpf.onFocusChangeListener = focusListener
        binding.etPhone.onFocusChangeListener = focusListener

        binding.vpCardFields.setOnTouchListener { p0, p1 -> true }
    }

}

@BindingAdapter("type")
fun EditText.createFieldMask(cardField: CreditCardField) {

    when(cardField.type) {
        CreditCardFieldType.NUMBER -> addTextChangedListener(Mask.mask("#### #### #### ####", this))
        CreditCardFieldType.EX_DATE -> addTextChangedListener(Mask.mask("##/##", this))
        CreditCardFieldType.CVV -> addTextChangedListener(Mask.mask("####", this))
        CreditCardFieldType.CPF -> addTextChangedListener(Mask.mask("###.###.###-##", this))
        CreditCardFieldType.PHONE -> addTextChangedListener(Mask.mask("## #####-####", this))
    }

    inputType = when(cardField.type) {
        CreditCardFieldType.NUMBER -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.NAME -> InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        CreditCardFieldType.EX_DATE -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.CVV -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.CPF -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.PHONE -> InputType.TYPE_CLASS_PHONE
    }

    setOnEditorActionListener { textView, i, keyEvent ->
        if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE){
            !cardField.validation(cardField, true)
        } else false
    }

}

@BindingAdapter("hint")
fun TextInputLayout.createFieldHint(type: CreditCardFieldType) {
    hint = when(type) {
        CreditCardFieldType.NUMBER -> context.getString(R.string.tuna_credit_card_number_label)
        CreditCardFieldType.NAME -> context.getString(R.string.tuna_credit_card_name_label)
        CreditCardFieldType.EX_DATE -> context.getString(R.string.tuna_credit_card_expiration_label)
        CreditCardFieldType.CVV -> context.getString(R.string.tuna_credit_card_cvv_label)
        CreditCardFieldType.CPF -> context.getString(R.string.tuna_credit_card_cpf_label)
        CreditCardFieldType.PHONE -> context.getString(R.string.tuna_credit_card_phone_label)
    }
}