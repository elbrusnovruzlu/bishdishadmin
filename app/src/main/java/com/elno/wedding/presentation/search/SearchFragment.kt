package com.elno.wedding.presentation.search

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
import com.elno.wedding.R
import com.elno.wedding.common.Constants
import com.elno.wedding.common.Resource
import com.elno.wedding.common.Static.filterModel
import com.elno.wedding.common.UtilityFunctions
import com.elno.wedding.databinding.FragmentSearchBinding
import com.elno.wedding.domain.model.VendorModel
import com.elno.wedding.presentation.adapter.VendorAdapter
import com.elno.wedding.presentation.base.BaseFragment
import com.elno.wedding.presentation.search.filter.FilterBottomSheetFragment
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
        adapter = VendorAdapter() {
            onOfferClick(it)
        }
        binding.gridView.adapter = adapter
        binding.gridView.layoutManager = GridLayoutManager(context, 2)
        binding.categoryChip.text = UtilityFunctions.getType(context, viewModel.categoryType)
        binding.categoryChip.isCloseIconVisible = viewModel.categoryType != "all"
        binding.redDot.isVisible = viewModel.categoryType != "all"
        viewModel.getVendorList()
        viewModel.getFilterMaxPrice()
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
        binding.priceChip.setOnClickListener {
            openFilterDialog()
        }
        binding.categoryChip.setOnCloseIconClickListener {
            viewModel.categoryType = "all"
            setFilterIcon(viewModel.categoryType, viewModel.minPrice, viewModel.maxPrice)
            viewModel.getVendorList()
        }
        binding.priceChip.setOnCloseIconClickListener {
            viewModel.minPrice = 0L
            viewModel.maxPrice = filterModel.maxPrice
            setFilterIcon(viewModel.categoryType, viewModel.minPrice, viewModel.maxPrice)
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
        viewModel.filterMaxPriceResult.observe(
            viewLifecycleOwner,
            ::consumeFilterMaxPriceResult
        )
    }

    private fun consumeFilterMaxPriceResult(filterMaxPrice: Long?) {
        setFilterIcon(viewModel.categoryType, viewModel.minPrice, viewModel.maxPrice)
    }

    private fun consumeVendorListResult(resource: Resource<ArrayList<VendorModel?>>) {
        when(resource) {
            is  Resource.Loading -> {
                binding.gridView.isVisible = false
                binding.vendorShimmerView.isVisible = true
                binding.vendorShimmerView.startShimmer()
            }
            is  Resource.Success -> {
                resource.data?.let {
                    binding.searchView.setOnQueryTextListener(null)
                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.setOnQueryTextListener(this)
                    adapter?.submitList(it)
                }
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
        val dialog = FilterBottomSheetFragment(viewModel.categoryType, viewModel.minPrice, viewModel.maxPrice) { categoryType, minPrice, maxPrice ->
            onFilterResult(categoryType, minPrice, maxPrice)
        }
        dialog.show(parentFragmentManager, FilterBottomSheetFragment::class.java.canonicalName)
    }

    private fun onFilterResult(categoryType: String, minPrice: Long, maxPrice: Long) {
        setFilterIcon(categoryType, minPrice, maxPrice)
        viewModel.categoryType = categoryType
        viewModel.minPrice = minPrice
        viewModel.maxPrice = maxPrice
        viewModel.getVendorList()
    }

    private fun setFilterIcon(categoryType: String, minPrice: Long, maxPrice: Long) {
        binding.categoryChip.text = UtilityFunctions.getType(context, categoryType)
        if(minPrice != 0L || maxPrice != filterModel.maxPrice) {
            binding.priceChip.isVisible = true
            binding.priceChip.text = "$minPrice ₼ - $maxPrice ₼"
        }
        else{
            binding.priceChip.isVisible = false
        }
        binding.categoryChip.isCloseIconVisible = categoryType != "all"
        binding.redDot.isVisible = categoryType != "all" || minPrice != 0L || maxPrice != filterModel.maxPrice
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