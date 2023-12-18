package com.elno.bishdish.presentation.favourite

import androidx.lifecycle.ViewModel
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.elno.bishdish.domain.model.VendorModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
): ViewModel() {

    private val _favouriteListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = SingleLiveData()
    val favouriteListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = _favouriteListResult


    fun getFavouriteList() {
        _favouriteListResult.value = Resource.Loading()
        databaseReference.collection("vendors").get().addOnSuccessListener { result ->
            val offerList = arrayListOf<VendorModel?>()
            for (documentSnapshot in result) {
                val offerModel = documentSnapshot.toObject(VendorModel::class.java)
                offerList.add(offerModel)
            }
            offerList.sortBy {it?.order}
            _favouriteListResult.value = Resource.Success(offerList)
        }.addOnFailureListener{
            _favouriteListResult.value = Resource.Error("Error while loading")
        }
    }

}