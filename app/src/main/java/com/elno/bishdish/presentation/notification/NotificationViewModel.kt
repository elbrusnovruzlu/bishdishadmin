package com.elno.bishdish.presentation.notification

import androidx.lifecycle.ViewModel
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.elno.bishdish.domain.model.NotificationModel
import com.elno.bishdish.domain.model.VendorModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
) : ViewModel() {

    private val _notificationListResult: SingleLiveData<Resource<ArrayList<NotificationModel?>>> = SingleLiveData()
    val notificationListResult: SingleLiveData<Resource<ArrayList<NotificationModel?>>> = _notificationListResult


    private val _vendorResult: SingleLiveData<Resource<VendorModel?>> = SingleLiveData()
    val vendorResult: SingleLiveData<Resource<VendorModel?>> = _vendorResult

    fun getNotificationList(time: Long) {
        _notificationListResult.value = Resource.Loading()
        databaseReference.collection("notification").whereGreaterThanOrEqualTo("time", time).get().addOnSuccessListener { result ->
            val notificationList = arrayListOf<NotificationModel?>()
            for (documentSnapshot in result) {
                val notificationModel = documentSnapshot.toObject(NotificationModel::class.java)
                notificationList.add(notificationModel)
            }
            notificationList.reverse()
            _notificationListResult.value = Resource.Success(notificationList)
        }.addOnFailureListener{
            _notificationListResult.value = Resource.Error("Error while loading")
        }
    }

    fun getVendor(vendorId: String) {
        _vendorResult.value = Resource.Loading()
        databaseReference.collection("vendors").document(vendorId).get().addOnSuccessListener { result ->
            val vendorModel = result.toObject(VendorModel::class.java)
            _vendorResult.value = Resource.Success(vendorModel)
        }.addOnFailureListener {
            _vendorResult.value = Resource.Error("Error while loading")
        }
    }

}