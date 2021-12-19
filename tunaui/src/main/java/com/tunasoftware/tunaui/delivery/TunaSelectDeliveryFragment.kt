package com.tunasoftware.tunaui.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.tunasoftware.tunacommons.ui.entities.UIState
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUIViewModelFactory
import com.tunasoftware.tunaui.databinding.FragmentTunaSelectDeliveryBinding
import com.tunasoftware.tunaui.utils.announceForAccessibility
import com.tunasoftware.tunaui.utils.setAsHeading

class TunaSelectDeliveryFragment : Fragment() {

    lateinit var viewModel: TunaSelectDeliveryViewModel
    lateinit var binding: FragmentTunaSelectDeliveryBinding
    lateinit var shimmerSelectDelivery: ShimmerFrameLayout

    private val adapter = SelectDeliveryAdapter()

    companion object {
        fun newInstance() = TunaSelectDeliveryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTunaSelectDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, TunaUIViewModelFactory(requireActivity().application))[TunaSelectDeliveryViewModel::class.java]

        binding.tunaToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.tuna_ic_arrow_back)
            navigationContentDescription = getString(R.string.tuna_accessibility_close_button)
            setNavigationOnClickListener { activity?.finish() }
            setTitle(R.string.tuna_select_delivery_title)
        }

        binding.tvTitle.setAsHeading()
        shimmerSelectDelivery = binding.root.findViewById(R.id.shimmer_select_delivery)

        binding.btnSelect.setOnClickListener {
            viewModel.onSubmitClick()
        }

        adapter.setOnClickListener {
            viewModel.onDeliverySelected(it)
            binding.rvDeliveries.postDelayed({
                val holder = binding.rvDeliveries.findViewHolderForAdapterPosition(adapter.getItemPosition(it))
                holder?.itemView?.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
            }, 1000)
        }

        val recyclerView = binding.rvDeliveries
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = null
        subscribe()
        viewModel.init()
    }

    private fun subscribe() {
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
                    context?.announceForAccessibility(getString(R.string.tuna_accessibility_error_loading_list))
                    stopShimmer()
                }
            }
        })

        viewModel.actionsLiveData.observe(this , { action ->
            when (action){
                is ActionOnDeliverySelected -> {
                    requireActivity().let {
                        if (it is TunaDeliverySelectionResultHandler){
                            it.onSelectedDelivery(action.delivery)
                        }
                    }
                }
            }
        })
    }

    private fun startShimmer() {
        binding.rvDeliveries.visibility = View.GONE
        binding.btnSelect.visibility = View.GONE
        shimmerSelectDelivery.startShimmer()
    }

    private fun stopShimmer() {
        binding.rvDeliveries.visibility = View.VISIBLE
        binding.btnSelect.visibility = View.VISIBLE
        shimmerSelectDelivery.stopShimmer()
        shimmerSelectDelivery.visibility = View.GONE
    }
}