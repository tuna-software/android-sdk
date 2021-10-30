package com.tunasoftware.tunaui.creditcard

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.databinding.BindingAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.tunasoftware.tunacr.TunaCardRecognition
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUIViewModelFactory
import com.tunasoftware.tunaui.closeKeyboard
import com.tunasoftware.tunaui.databinding.NewCardFragmentBinding
import com.tunasoftware.tunaui.extensions.*
import com.tunasoftware.tunaui.navigator
import com.tunasoftware.tunaui.utils.Mask
import com.tunasoftware.tunaui.utils.announceForAccessibility
import com.tunasoftware.tunaui.utils.disableForAccessibility


open class NewCardFragment : DialogFragment() {

    companion object {
        fun newInstance() = NewCardFragment()

        const val RESULT_CARD = "RESULT_CARD"
    }

    private lateinit var viewModel: NewCardViewModel
    private lateinit var binding: NewCardFragmentBinding
    private lateinit var listOfFields : List<EditText>

    private var animSetRightOut: AnimatorSet? = null
    private var animSetLeftIn: AnimatorSet? = null
    private var animSetRightOutReverse: AnimatorSet? = null
    private var animSetLeftInReverse: AnimatorSet? = null

    private var isCardFlipped = false
    private var isCardCpfFlipped = false

    protected var showCpfField : Boolean = false

    private var resultCardRecognitionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, TunaUIViewModelFactory(requireActivity().application))[NewCardViewModel::class.java]
        binding = NewCardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        arguments?.let { NewCardFragmentArgs.fromBundle(it) }?.let {
            showCpfField = it.shouldShowCpf
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tunaToolbar.apply {
            navigationIcon = getNavigationIconDrawable()
            navigationContentDescription = getString(R.string.tuna_accessibility_back_button)
            setNavigationOnClickListener { navigator.navigateUp() }
            setTitle(R.string.tuna_add_credit_card)
        }

        binding.flCards.disableForAccessibility()
        binding.widgetCardCpf.disableForAccessibility()
        binding.widgetCardBack.disableForAccessibility()

        subscribe()
        setupFields()
        loadAnimations()
        setupFooterBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.closeKeyboard()
    }

    open fun getNavigationIconDrawable(): Drawable? {
        return ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_arrow_back)
    }

    open fun getCurrentFocus(): View? {
        return requireActivity().currentFocus
    }

    private fun setupFields(){
        val fieldWidth = resources.getBoolean(R.bool.tuna_new_card_dialog_fragment_window_is_floating).let {
            if (it) {
                resources.getDimension(R.dimen.tuna_select_payment_method_layout_width).toInt() - 48.dp
            } else {
                requireContext().displayMetrics.widthPixels - 48.dp
            }
        }

        listOfFields = listOf(
            binding.etNumber,
            binding.etName,
            binding.etExDate,
            binding.etCvv,
        )
        if (showCpfField){
            listOfFields = listOfFields + binding.etCpf
            binding.etCpf.layout(width = fieldWidth)
        } else {
            binding.etCpf.visibility = GONE
        }

        binding.etNumber.layout(width = fieldWidth)
        binding.etName.layout(width = fieldWidth)
        binding.etExDate.layout(width = fieldWidth)
        binding.etCvv.layout(width = fieldWidth)

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
                        it.setOnTouchListener { _, _ -> true }
                    } else {
                        it.setOnTouchListener { _, _ -> false }
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

        // Block touching next field
        binding.vpCardFields.setOnTouchListener { _, _ -> true }

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

    private fun subscribe() {
        viewModel.actionsLiveData.observe(this, { action ->
            when (action) {
                is ActionFieldBack -> {
                    getCurrentFocus()?.apply {
                        when (id) {
                            R.id.etName -> binding.etNumber.requestFocus()
                            R.id.etExDate -> binding.etName.requestFocus()
                            R.id.etCvv -> binding.etExDate.requestFocus()
                            R.id.etCpf -> binding.etCvv.requestFocus()
                        }
                    }
                }
                is ActionFieldNext -> {
                    when (action.currentType) {
                        CreditCardFieldType.NUMBER -> binding.etName.requestFocus()
                        CreditCardFieldType.NAME -> binding.etExDate.requestFocus()
                        CreditCardFieldType.EX_DATE -> binding.etCvv.requestFocus()
                        CreditCardFieldType.CVV -> binding.etCpf.requestFocus()
                        else -> {}
                    }
                }
                is ActionFinish -> {
                    setNavigationResult(action.card, RESULT_CARD)
                    findNavController().navigateUp()
                    context?.announceForAccessibility(getString(R.string.tuna_accessibility_saved_card))
                }
            }
        })
    }

    private fun loadAnimations() {
        val scale = requireContext().resources.displayMetrics.density
        binding.widgetCard.cameraDistance = 8000 * scale
        binding.widgetCardBack.cameraDistance = 8000 * scale
        binding.widgetCardCpf.cameraDistance = 8000 * scale

        animSetRightOut = AnimatorInflater.loadAnimator(requireContext(), R.animator.out_animation) as AnimatorSet
        animSetRightOut?.setTarget(binding.widgetCard)

        animSetLeftIn = AnimatorInflater.loadAnimator(requireContext(), R.animator.in_animation) as AnimatorSet
        animSetLeftIn?.setTarget(binding.widgetCardBack)

        animSetRightOutReverse = AnimatorInflater.loadAnimator(requireContext(), R.animator.out_animation_reverse) as AnimatorSet
        animSetRightOutReverse?.setTarget(binding.widgetCard)

        animSetLeftInReverse = AnimatorInflater.loadAnimator(requireContext(), R.animator.in_animation_reverse) as AnimatorSet
        animSetLeftInReverse?.setTarget(binding.widgetCardBack)
    }

    private fun flipCards() {
        binding.widgetCardCpf.visibility = GONE

        isCardFlipped = if (!isCardFlipped) {
            // Start
            animSetRightOut?.start()
            animSetLeftIn?.start()
            true
        } else {
            // Reverse
            animSetRightOutReverse?.start()
            animSetLeftInReverse?.start()
            false
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

        binding.widgetCardCpf.visibility = VISIBLE

        isCardCpfFlipped = if (!isCardCpfFlipped) {
            animSetSlideRightOut = ObjectAnimator
                .ofFloat(binding.widgetCardBack, "translationX", 0f, (displayWidth * -1.0f))
                .setDuration(duration)

            animSetSlideLeftIn = ObjectAnimator
                .ofFloat(binding.widgetCardCpf, "translationX", displayWidth, 0f)
                .setDuration(duration)
            true
        } else {
            animSetSlideRightOut = ObjectAnimator
                .ofFloat(binding.widgetCardBack, "translationX", (displayWidth * -1.0f), 0f)
                .setDuration(duration)

            animSetSlideLeftIn = ObjectAnimator
                .ofFloat(binding.widgetCardCpf, "translationX", 0f, displayWidth)
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
        CreditCardFieldType.NUMBER -> addTextChangedListener(Mask.mask("#### #### #### #### ###"))
        CreditCardFieldType.EX_DATE -> addTextChangedListener(Mask.mask("##/##"))
        CreditCardFieldType.CVV -> addTextChangedListener(Mask.mask("####"))
        CreditCardFieldType.CPF -> addTextChangedListener(Mask.mask("###.###.###-##"))
        else -> {}
    }

    inputType = when(cardField.type) {
        CreditCardFieldType.NUMBER -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.NAME -> InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        CreditCardFieldType.EX_DATE -> InputType.TYPE_CLASS_DATETIME
        CreditCardFieldType.CVV -> InputType.TYPE_CLASS_PHONE
        CreditCardFieldType.CPF -> InputType.TYPE_CLASS_PHONE
    }

    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE){
            val result = cardField.validation(cardField, true)
            if (result) cardField.next(cardField)
        }
        true
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