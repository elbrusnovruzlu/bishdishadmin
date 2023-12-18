package com.elno.bishdish.presentation.search

import androidx.lifecycle.ViewModel
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.elno.bishdish.common.Static
import com.elno.bishdish.domain.model.VendorModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
) : ViewModel() {

    var categoryType = "all"
    var ingredientType = arrayListOf<String>()

    private val _vendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> =
        SingleLiveData()
    val vendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = _vendorListResult

    fun getFilterMaxPrice() {
    }

    fun getVendorList() {
        _vendorListResult.value = Resource.Loading()
        val collectionReference =
            if (categoryType == "all") databaseReference.collection("vendors")
            else databaseReference.collection("vendors").whereArrayContains("type", categoryType)
        collectionReference.get().addOnSuccessListener { result ->
            val vendorList = arrayListOf<VendorModel?>()
            for (documentSnapshot in result) {
                val vendorModel = documentSnapshot.toObject(VendorModel::class.java)
                if(vendorModel.ingredients?.containsAll(ingredientType) == true) vendorList.add(vendorModel)
            }
            vendorList.sortBy {it?.order}
            _vendorListResult.value = Resource.Success(vendorList)
        }.addOnFailureListener {
            _vendorListResult.value = Resource.Error("Error while loading")
        }
    }

}