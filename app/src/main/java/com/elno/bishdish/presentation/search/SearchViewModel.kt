package com.elno.bishdish.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.filters
import com.algolia.search.dsl.query
import com.algolia.search.helper.deserialize
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.NumericOperator
import com.elno.bishdish.common.Resource
import com.elno.bishdish.common.SingleLiveData
import com.elno.bishdish.domain.model.VendorModel
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

    fun searchVendorList(isMore: Boolean = false) {
        if(!isMore) _vendorListResult.value = Resource.Loading()
        viewModelScope.launch {
            val index = client.initIndex(indexName = IndexName("vendors"))
            val response = index.run {
                val query = query {
                    filters {
                        and {
                            if(categoryType != "all") facet("type", categoryType)
                            ingredientType.forEach {
                                facet("ingredients", it)
                            }
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


}