package com.tunasoftware.tunaui.document

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.databinding.FragmentTunaInformDocumentBinding
import com.tunasoftware.tunaui.extensions.isCpf
import com.tunasoftware.tunaui.utils.Mask
import com.tunasoftware.tunaui.utils.setAsHeading

class TunaInformDocumentFragment : Fragment() {

    lateinit var binding: FragmentTunaInformDocumentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTunaInformDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tunaToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_arrow_back)
            navigationContentDescription = getString(R.string.tuna_accessibility_close_button)
            setNavigationOnClickListener { activity?.finish() }
            setTitle(R.string.tuna_inform_document_title)
        }

        binding.tvTitle.setAsHeading()

        binding.btnSelect.setOnClickListener { handleInformDocument() }

        binding.etNumber.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                handleInformDocument()
            }
            true
        }

        binding.etNumber.addTextChangedListener(Mask.mask("###.###.###-##"))
        binding.etNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tiNumber.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun handleInformDocument() {
        val document = binding.etNumber.text.toString()

        if (!document.isCpf()) {
            binding.tiNumber.error = context?.getString(R.string.tuna_card_cpf_invalid)
            return
        }

        requireActivity().let {
            if (it is TunaInformDocumentResultHandler) {
                it.onDocumentInformed(document)
            }
        }
    }
}