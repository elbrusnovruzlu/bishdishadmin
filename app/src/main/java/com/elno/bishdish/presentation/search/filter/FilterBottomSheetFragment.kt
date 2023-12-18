package com.elno.bishdish.presentation.search.filter

import android.os.Bundle
import android.view.View
import android.view.View.NO_ID
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.core.view.isVisible
import com.elno.bishdish.R
import com.elno.bishdish.common.Static.filterModel
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.databinding.FragmentFilterBottomSheetBinding
import com.elno.bishdish.domain.model.CategoryModel
import com.elno.bishdish.domain.model.IngredientModel
import com.elno.bishdish.presentation.base.BaseDialogFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterBottomSheetFragment(
    private var categoryType: String,
    private var ingredientType: ArrayList<String>,
    private val onClick: (categoryType: String, ingredientType: ArrayList<String>) -> Unit
    ) : BaseDialogFragment<FragmentFilterBottomSheetBinding>(FragmentFilterBottomSheetBinding::inflate), SearchView.OnQueryTextListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(this)
        binding.rootView.setOnClickListener {
            dismiss()
        }
        binding.container.setOnClickListener(null)
        binding.close.setOnClickListener {
            dismiss()
        }

        filterModel.categories?.forEach {
            addToChipGroup(it)
        }

        filterModel.ingredient?.forEach {
            addToIngredientChipGroup(it)
        }

        binding.filterBtn.setOnClickListener {
            val chipId = binding.chipGroup.checkedChipId
            val type = if(chipId == NO_ID) {
                "all"
            } else {
                val chip: Chip = binding.chipGroup.findViewById(chipId)
                chip.tag as String
            }
            val ingredientList  = binding.ingredientChipGroup.children.filter { (it as Chip).isChecked }.map { it.tag as String }
            onClick(type, ArrayList(ingredientList.toList()))
            dismiss()
        }
        binding.clearFilterBtn.setOnClickListener {
            onClick("all", arrayListOf())
            dismiss()
        }
    }

    private fun addToChipGroup(categoryModel: CategoryModel?) {
        val chip = layoutInflater.inflate(R.layout.chip_item, binding.chipGroup, false) as Chip
        chip.text = getLocalizedTextFromMap(context, categoryModel?.name)
        chip.isChecked = categoryType == categoryModel?.type
        chip.tag = categoryModel?.type
        binding.chipGroup.addView(chip)
    }

    private fun addToIngredientChipGroup(ingredientModel: IngredientModel?) {
        val chip = layoutInflater.inflate(R.layout.chip_item, binding.chipGroup, false) as Chip
        chip.text = getLocalizedTextFromMap(context, ingredientModel?.name)
        chip.isChecked = ingredientType.contains(ingredientModel?.type)
        chip.tag = ingredientModel?.type
        binding.ingredientChipGroup.addView(chip)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        binding.ingredientChipGroup.children.forEach {
            it.isVisible = newText.isNullOrEmpty() || ((it as Chip).text.toString()).lowercase().contains(newText.toString().lowercase())
        }
        return false
    }

}