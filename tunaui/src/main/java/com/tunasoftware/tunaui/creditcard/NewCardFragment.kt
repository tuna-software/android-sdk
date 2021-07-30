package com.tunasoftware.tunaui.creditcard

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.*
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.tunasoftware.tunacr.TunaCardRecognition
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.NewCardFragmentBinding
import com.tunasoftware.tunaui.extensions.*
import com.tunasoftware.tunaui.navigator
import com.tunasoftware.tunaui.utils.Mask
import kotlinx.android.synthetic.main.new_card_fragment.*


class NewCardFragment : Fragment() {

    companion object {
        fun newInstance() = NewCardFragment()

        const val  RESULT_CARD = "RESULT_CARD"
    }

    private lateinit var viewModel: NewCardViewModel
    private lateinit var binding: NewCardFragmentBinding
    private lateinit var listOfFields : List<EditText>

    private var animSetRightOut: AnimatorSet? = null
    private var animSetLeftIn: AnimatorSet? = null

    private var isCardFlipped = false
    private var isCardCpfFlipped = false

    var resultCardRecognitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            result.data?.run {
                val name = getStringExtra(TunaCardRecognition.RESULT_NAME)?:""
                val number = getStringExtra(TunaCardRecognition.RESULT_NUMBER)?:""
                val expiration = getStringExtra(TunaCardRecognition.RESULT_EXPIRATION)?:""
                viewModel.setCardData(name, number, expiration)
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(NewCardViewModel::class.java)
        binding = NewCardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tuna_toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.tuna_ic_arrow_back
            )
            setNavigationOnClickListener {
                navigator.navigateUp()
            }
            setTitle(R.string.tuna_add_credit_card)
        }
        subscribe()
        setupFields()
        loadAnimations()
        setupFooterBar()
    }

    private fun setupFields(){
        val fieldWidth = requireContext().displayMetrics.widthPixels - 48.dp
        listOfFields = listOf(
            binding.etNumber,
            binding.etName,
            binding.etExDate,
            binding.etCvv,
            binding.etCpf
        )

        binding.etNumber.layout(width = fieldWidth)
        binding.etName.layout(width = fieldWidth)
        binding.etExDate.layout(width = fieldWidth)
        binding.etCvv.layout(width = fieldWidth)
        binding.etCpf.layout(width = fieldWidth)

        val focusListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                when (view.id) {
                    R.id.etName, R.id.etNumber, R.id.etExDate -> {
                        if (isCardFlipped) {
                            flipCards()
                        }
                    }
                    R.id.etCvv -> {
                        if (!isCardFlipped) {
                            flipCards()
                        }

                        if (isCardCpfFlipped) {
                            flipCardCpf()
                        }
                    }
                    R.id.etCpf -> {
                        if (!isCardCpfFlipped) {
                            flipCardCpf()
                        }
                    }
                }
                binding.vpCardFields.smoothScrollTo(view.textLayoutParent().x.toInt(), 0)
                listOfFields.forEach {
                    if (it != view) {
                        it.setOnTouchListener { view, motionEvent -> true }
                    } else {
                        it.setOnTouchListener { view, motionEvent -> false }
                    }
                }

                binding.tunaFooterBar.currentStep = listOfFields.indexOf(view)
            }
        }

        binding.etNumber.onFocusChangeListener = focusListener
        binding.etName.onFocusChangeListener = focusListener
        binding.etExDate.onFocusChangeListener = focusListener
        binding.etCvv.onFocusChangeListener = focusListener
        binding.etCpf.onFocusChangeListener = focusListener

        binding.vpCardFields.setOnTouchListener { p0, p1 -> true }

        binding.flCards.setOnClickListener {
            viewModel.onPreviousClick()
        }
        binding.btnCamera.setOnClickListener {
            Intent().apply {
                action = "TUNA_OPEN_CARD_RECOGNITION"
            }.run {
                val packageManager = requireActivity().packageManager
                if (resolveActivity(packageManager) != null) {
                    resultCardRecognitionLauncher.launch(this)
                    requireActivity().overridePendingTransition(R.anim.modal_in, R.anim.no_change)
                } else {
                    Log.d("TUNA", "No Intent available to handle action")
                }
            }
        }
    }

    fun subscribe() {
        viewModel.actionsLiveData.observe(this, { action ->
            when (action) {
                is ActionFieldBack -> {
                    requireActivity().currentFocus?.apply {
                        when (id) {
                            R.id.etName -> binding.etNumber.requestFocus()
                            R.id.etExDate -> binding.etName.requestFocus()
                            R.id.etCvv -> binding.etExDate.requestFocus()
                            R.id.etCpf -> binding.etCvv.requestFocus()
                        }
                    }
                }
                is ActionFieldNext -> {
                    if (requireActivity().currentFocus is EditText) {
                        val editText = requireActivity().currentFocus as EditText
                        editText.onEditorAction(editText.imeOptions)
                    }
                }
                is ActionFinish -> {
                    setNavigationResult(action.card, RESULT_CARD)
                    findNavController().navigateUp()
                }
            }
        })
    }


    private fun loadAnimations() {
        val scale = requireContext().resources.displayMetrics.density
        widgetCard.cameraDistance = 8000 * scale
        widgetCardBack.cameraDistance = 8000 * scale
        widgetCardCpf.cameraDistance = 8000 * scale

        animSetRightOut = AnimatorInflater.loadAnimator(requireContext(), R.animator.out_animation) as AnimatorSet
        animSetLeftIn = AnimatorInflater.loadAnimator(requireContext(), R.animator.in_animation) as AnimatorSet

    }

    private fun flipCards() {
        widgetCardCpf.visibility = GONE
        animSetRightOut?.setTarget(widgetCard)
        animSetLeftIn?.setTarget(widgetCardBack)
        if (!isCardFlipped) {
            animSetRightOut?.start()
            animSetLeftIn?.start()
            isCardFlipped = true
        } else {
            animSetRightOut?.reverse()
            animSetLeftIn?.reverse()
            isCardFlipped = false
        }
    }

    private fun flipCardCpf() {
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val displayWidth = size.x.toFloat()

        val animSetSlideRightOut: ObjectAnimator
        val animSetSlideLeftIn: ObjectAnimator

        val duration: Long = 500

        widgetCardCpf.visibility = VISIBLE

        isCardCpfFlipped = if (!isCardCpfFlipped) {
            animSetSlideRightOut = ObjectAnimator
                .ofFloat(widgetCardBack, "translationX", 0f, (displayWidth * -1.0f))
                .setDuration(duration)

            animSetSlideLeftIn = ObjectAnimator
                .ofFloat(widgetCardCpf, "translationX", displayWidth, 0f)
                .setDuration(duration)
            true
        } else {
            animSetSlideRightOut = ObjectAnimator
                .ofFloat(widgetCardBack, "translationX", (displayWidth * -1.0f), 0f)
                .setDuration(duration)

            animSetSlideLeftIn = ObjectAnimator
                .ofFloat(widgetCardCpf, "translationX", 0f, displayWidth)
                .setDuration(duration)
            false
        }

        animSetSlideRightOut.interpolator = OvershootInterpolator(0.5f)
        animSetSlideLeftIn.interpolator = OvershootInterpolator(0.5f)

        animSetSlideRightOut.start()
        animSetSlideLeftIn.start()
    }

    private fun setupFooterBar() {
        binding.tunaFooterBar.steps = listOfFields.size

        binding.tunaFooterBar.setOnPreviousClickListener {
            viewModel.onPreviousClick()
        }

        binding.tunaFooterBar.setOnNextClickListener {
            viewModel.onNextClick(
                when (binding.tunaFooterBar.currentStep) {
                    0 -> CreditCardFieldType.NUMBER
                    1 -> CreditCardFieldType.NAME
                    2 -> CreditCardFieldType.EX_DATE
                    3 -> CreditCardFieldType.CVV
                    else -> CreditCardFieldType.CPF
                }
            )
        }
    }

}

@BindingAdapter("type")
fun EditText.createFieldMask(cardField: CreditCardField) {

    when(cardField.type) {
        CreditCardFieldType.NUMBER -> addTextChangedListener(Mask.mask("#### #### #### #### ###", this))
        CreditCardFieldType.EX_DATE -> addTextChangedListener(Mask.mask("##/##", this))
        CreditCardFieldType.CVV -> addTextChangedListener(Mask.mask("####", this))
        CreditCardFieldType.CPF -> addTextChangedListener(Mask.mask("###.###.###-##", this))
    }

    inputType = when(cardField.type) {
        CreditCardFieldType.NUMBER -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.NAME -> InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        CreditCardFieldType.EX_DATE -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.CVV -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.CPF -> InputType.TYPE_CLASS_PHONE
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
    }
}