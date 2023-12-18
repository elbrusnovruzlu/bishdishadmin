package com.elno.bishdish.presentation.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.elno.bishdish.R
import com.elno.bishdish.common.Constants
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.Static.filterModel
import com.elno.bishdish.common.UtilityFunctions
import com.elno.bishdish.databinding.FragmentSearchBinding
import com.elno.bishdish.domain.model.VendorModel
import com.elno.bishdish.presentation.adapter.VendorAdapter
import com.elno.bishdish.presentation.base.BaseFragment
import com.elno.bishdish.presentation.search.filter.FilterBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate), SearchView.OnQueryTextListener {

    private val viewModel: SearchViewModel by viewModels()
    private var adapter: VendorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        if(sharedPreferences?.contains(Constants.CATEGORY_TYPE) == true) {
            viewModel.categoryType = sharedPreferences.getString(Constants.CATEGORY_TYPE, "all") ?: "all"
            sharedPreferences.edit().remove(Constants.CATEGORY_TYPE).apply()
        }
    }

    override fun setupViews() {
        adapter = VendorAdapter(
            { onOfferClick(it) },
            { onEmptyResult(it) },
            requireContext()
        )
        binding.gridView.adapter = adapter
        binding.gridView.layoutManager = GridLayoutManager(context, 2)
        binding.categoryChip.text = UtilityFunctions.getType(context, viewModel.categoryType)
        binding.categoryChip.isCloseIconVisible = viewModel.categoryType != "all"
        binding.ingredientChip.isCloseIconVisible = viewModel.ingredientType.isNotEmpty()
        binding.redDot.isVisible = viewModel.categoryType != "all"
        viewModel.getVendorList()
        viewModel.getFilterMaxPrice()
    }

    private fun onEmptyResult(isEmpty: Boolean) {
        binding.emptyLayout.isVisible = isEmpty
    }

    override fun setupListeners() {
        binding.filter.setOnClickListener {
            openFilterDialog()
        }
        binding.redDot.setOnClickListener {
            openFilterDialog()
        }
        binding.categoryChip.setOnClickListener {
            openFilterDialog()
        }
        binding.ingredientChip.setOnClickListener {
            openFilterDialog()
        }
        binding.categoryChip.setOnCloseIconClickListener {
            viewModel.categoryType = "all"
            setFilterIcon(viewModel.categoryType, viewModel.ingredientType)
            binding.emptyLayout.isVisible = false
            viewModel.getVendorList()
        }
        binding.ingredientChip.setOnCloseIconClickListener {
            viewModel.ingredientType = arrayListOf()
            setFilterIcon(viewModel.categoryType, viewModel.ingredientType)
            binding.emptyLayout.isVisible = false
            viewModel.getVendorList()
        }
        binding.favourite.setOnClickListener {
            findNavController().navigate(R.id.favouriteFragment)
        }
        binding.searchView.setOnQueryTextListener(this)
    }

    override fun setupObservers() {
        viewModel.vendorListResult.observe(
            viewLifecycleOwner,
            ::consumeVendorListResult
        )
    }

    private fun consumeVendorListResult(resource: Resource<ArrayList<VendorModel?>>) {
        when(resource) {
            is  Resource.Loading -> {
                binding.gridView.isVisible = false
                binding.vendorShimmerView.isVisible = true
                binding.vendorShimmerView.startShimmer()
            }
            is  Resource.Success -> {
                binding.emptyLayout.isVisible = resource.data.isNullOrEmpty()
                binding.searchView.setOnQueryTextListener(null)
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                binding.searchView.setOnQueryTextListener(this)
                adapter?.submitList(resource.data?: mutableListOf())
                binding.vendorShimmerView.stopShimmer()
                binding.gridView.isVisible = true
                binding.vendorShimmerView.isVisible = false
            }
            is  Resource.Error -> {
                binding.vendorShimmerView.stopShimmer()
                binding.vendorShimmerView.isVisible = false
                Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onOfferClick(model: VendorModel?) {
        findNavController().navigate(
            R.id.offerInfoFragment, bundleOf(
                Constants.OFFER_MODEL to model
            )
        )
    }

    private fun openFilterDialog() {
        val dialog = FilterBottomSheetFragment(viewModel.categoryType, viewModel.ingredientType) { categoryType, ingredientType ->
            onFilterResult(categoryType, ingredientType)
        }
        dialog.show(parentFragmentManager, FilterBottomSheetFragment::class.java.canonicalName)
    }

    private fun onFilterResult(categoryType: String, ingredientType: ArrayList<String>) {
        setFilterIcon(categoryType, ingredientType)
        viewModel.categoryType = categoryType
        viewModel.ingredientType = ingredientType
        binding.emptyLayout.isVisible = false
        viewModel.getVendorList()
    }

    private fun setFilterIcon(categoryType: String, ingredientType: ArrayList<String>) {
        binding.categoryChip.text = UtilityFunctions.getType(context, categoryType)
        if(ingredientType.isNotEmpty()) {
            binding.ingredientChip.text = getString(R.string.clear_selected_ingredient)
        }
        else{
            binding.ingredientChip.text = getString(R.string.choose_ingredient)
        }
        binding.categoryChip.isCloseIconVisible = categoryType != "all"
        binding.ingredientChip.isCloseIconVisible = ingredientType.isNotEmpty()
        binding.redDot.isVisible = categoryType != "all" || ingredientType.isNotEmpty()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter?.filter?.filter(newText)
        return false
    }

    override fun onDestroyView() {
        binding.searchView.setOnQueryTextListener(null)
        super.onDestroyView()
    }
}