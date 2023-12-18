package com.elno.bishdish.presentation.more.policy

import androidx.lifecycle.ViewModel
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrivacyPolicyViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
) : ViewModel() {



    private val _policyResult: SingleLiveData<Resource<String?>> =
        SingleLiveData()
    val policyResult: SingleLiveData<Resource<String?>> = _policyResult

    fun getPolicy() {
        _policyResult.value = Resource.Loading()
        databaseReference.collection("policy").get().addOnSuccessListener { result ->
            var policy: String? = null
            for (documentSnapshot in result) {
                policy = documentSnapshot.data["policy"] as String
            }
            _policyResult.value = Resource.Success(policy)
        }.addOnFailureListener {
            _policyResult.value = Resource.Error("Error while loading")
        }
    }

}