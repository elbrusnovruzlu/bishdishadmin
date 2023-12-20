package com.elno.bishdishadmin.presentation.dashboard

import androidx.lifecycle.ViewModel
import com.elno.bishdishadmin.common.Resource
import com.elno.bishdishadmin.common.SingleLiveData
import com.elno.bishdishadmin.common.Static
import com.elno.bishdishadmin.domain.model.CategoryModel
import com.elno.bishdishadmin.domain.model.IngredientModel
import com.elno.bishdishadmin.domain.model.VendorModel
import com.elno.bishdishadmin.domain.model.SliderModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
): ViewModel() {

    private val _sliderListResult: SingleLiveData<Resource<ArrayList<SliderModel?>>> = SingleLiveData()
    val sliderListResult: SingleLiveData<Resource<ArrayList<SliderModel?>>> = _sliderListResult


    fun getSliderList() {
        _sliderListResult.value = Resource.Loading()
        databaseReference.collection("slider").get().addOnSuccessListener { result ->
            val sliderList = arrayListOf<SliderModel?>()
            for (documentSnapshot in result) {
                val sliderModel = documentSnapshot.toObject(SliderModel::class.java)
                sliderList.add(sliderModel)
            }
            _sliderListResult.value = Resource.Success(sliderList)
        }.addOnFailureListener{
            _sliderListResult.value = Resource.Error("Error while loading")
        }
    }

}