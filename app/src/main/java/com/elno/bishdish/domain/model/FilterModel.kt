package com.elno.bishdish.domain.model

data class FilterModel(
    var categories: ArrayList<CategoryModel?>? = null,
    var ingredient: ArrayList<IngredientModel?>? = null
)