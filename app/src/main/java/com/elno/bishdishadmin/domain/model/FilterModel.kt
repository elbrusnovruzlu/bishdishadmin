package com.elno.bishdishadmin.domain.model

data class FilterModel(
    var categories: ArrayList<CategoryModel?>? = null,
    var ingredient: ArrayList<IngredientModel?>? = null
)