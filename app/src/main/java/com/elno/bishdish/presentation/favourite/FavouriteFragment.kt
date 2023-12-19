package com.elno.bishdish.presentation.favourite

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.elno.bishdish.MainActivity
import com.elno.bishdish.R
import com.elno.bishdish.common.Constants
import com.elno.bishdish.common.Constants.FAVOURITE_LIST
import com.elno.bishdish.common.Resource
import com.elno.bishdish.data.local.LocalDataStore
import com.elno.bishdish.databinding.FragmentFavouriteBinding
import com.elno.bishdish.domain.model.VendorModel
import com.elno.bishdish.presentation.adapter.VendorAdapter
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>(FragmentFavouriteBinding::inflate) {

    private val viewModel: FavouriteViewModel by viewModels()

    private var adapter: VendorAdapter? = VendorAdapter(
        { onOfferClick(it) },
        {}
    )

    override fun setupViews() {
        adapter?.context = requireContext()
        binding.gridView.adapter = adapter
        binding.gridView.layoutManager = GridLayoutManager(context, 2)
        if(adapter?.itemCount == 0) {
            viewModel.getFavouriteList()
        } else {
            binding.gridView.isVisible = true
        }
    }

    override fun setupObservers() {
        viewModel.favouriteListResult.observe(
            viewLifecycleOwner,
            ::consumeFavouriteListResult
        )
    }

    private fun consumeFavouriteListResult(resource: Resource<ArrayList<VendorModel?>>?) {
        when(resource) {
            is  Resource.Loading -> {
                binding.loading.isVisible = true
            }
            is  Resource.Success -> {
                binding.loading.isVisible = false
                val favouriteIds = LocalDataStore(context).getList<String>(FAVOURITE_LIST)
                val filteredList = resource.data?.filter { favouriteIds.contains(it?.id) }?.toMutableList()
                if(filteredList.isNullOrEmpty()) {
                    binding.emptyLayout.isVisible = true
                    binding.gridView.isVisible = false
                }
                else {
                    binding.emptyLayout.isVisible = false
                    binding.gridView.isVisible = true
                    adapter?.submitList(filteredList)
                }
            }
            is  Resource.Error -> {
                binding.loading.isVisible = false
                Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.addBtn.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(R.id.searchFragment)
        }
    }

    private fun onOfferClick(model: VendorModel?) {
        findNavController().navigate(
            R.id.offerInfoFragment, bundleOf(
            Constants.OFFER_MODEL to model
        )
        )
    }

}