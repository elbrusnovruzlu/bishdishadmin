package com.elno.bishdish.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.attributesToRetrieve
import com.algolia.search.dsl.facetFilters
import com.algolia.search.dsl.filters
import com.algolia.search.dsl.query
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.NumericOperator
import com.algolia.search.model.index.Scope
import com.algolia.search.model.search.Query
import com.algolia.search.model.settings.Settings
import com.algolia.search.transport.RequestOptions
import com.elno.bishdish.common.LocaleManager
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.elno.bishdish.common.Static
import com.elno.bishdish.domain.model.VendorModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val databaseReference: FirebaseFirestore
) : ViewModel() {

    private val client = ClientSearch(
        applicationID = ApplicationID("27W2QSCKJA"),
        apiKey = APIKey("84f891650e14b307f57c864589a37c64")
    )

    var categoryType = "all"
    var ingredientType = arrayListOf<String>()
    var lastIndex: Int = 0
    var searchQuery = ""

    private val _vendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = SingleLiveData()
    val vendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = _vendorListResult

    private val _moreVendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = SingleLiveData()
    val moreVendorListResult: SingleLiveData<Resource<ArrayList<VendorModel?>>> = _moreVendorListResult


    fun getVendorList() {
        _vendorListResult.value = Resource.Loading()
        val collectionReference =
            if (categoryType == "all") databaseReference.collection("vendors")
            else databaseReference.collection("vendors").whereArrayContains("type", categoryType)
        collectionReference.orderBy("order").limit(20).get().addOnSuccessListener { result ->
            val vendorList = arrayListOf<VendorModel?>()
            for (documentSnapshot in result) {
                val vendorModel = documentSnapshot.toObject(VendorModel::class.java)
                if(vendorModel.ingredients?.containsAll(ingredientType) == true) vendorList.add(vendorModel)
            }
            lastIndex = if(vendorList.isEmpty()) Int.MAX_VALUE else vendorList.last()?.order?: Int.MAX_VALUE
            Log.d("TAHIRA", vendorList.map { it?.id }.toString())
            _vendorListResult.value = Resource.Success(vendorList)
        }.addOnFailureListener {
            _vendorListResult.value = Resource.Error("Error while loading")
        }
    }

    fun searchVendorList(isMore: Boolean = false) {
        if(!isMore) _vendorListResult.value = Resource.Loading()
        viewModelScope.launch {
            val index = client.initIndex(indexName = IndexName("vendors"))
            val response = index.run {
                val query = query {
                    filters {
                        and {
                            comparison("order", NumericOperator.Greater, lastIndex)
                        }
                    }
                }
                if(searchQuery.isNotEmpty()) query.query = searchQuery

                search(query)
            }
            val results: List<VendorModel> = response.hits.deserialize(VendorModel.serializer())
            lastIndex = if(results.isEmpty()) Int.MAX_VALUE else results.last().order?: Int.MAX_VALUE
            Log.d("TAHIRAA", results.map { it.id }.toString())
            if(!isMore) _vendorListResult.value = Resource.Success(ArrayList(results))
            else _moreVendorListResult.value = Resource.Success(ArrayList(results))
        }
    }

    fun getMoreVendorList() {
        val collectionReference =
            if (categoryType == "all") databaseReference.collection("vendors")
            else databaseReference.collection("vendors").whereArrayContains("type", categoryType)
        collectionReference.orderBy("order").whereGreaterThan("order", lastIndex).limit(20).get().addOnSuccessListener { result ->
            val vendorList = arrayListOf<VendorModel?>()
            for (documentSnapshot in result) {
                val vendorModel = documentSnapshot.toObject(VendorModel::class.java)
                if(vendorModel.ingredients?.containsAll(ingredientType) == true) vendorList.add(vendorModel)
            }
            lastIndex = if(vendorList.isEmpty()) Int.MAX_VALUE else vendorList.last()?.order?: Int.MAX_VALUE
            Log.d("TAHIRA", vendorList.map { it?.id }.toString())
            _moreVendorListResult.value = Resource.Success(vendorList)
        }.addOnFailureListener {
            _moreVendorListResult.value = Resource.Error("Error while loading")
        }
    }

}