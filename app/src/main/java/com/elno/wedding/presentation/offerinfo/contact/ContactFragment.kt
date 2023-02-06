package com.elno.wedding.presentation.offerinfo.contact

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.elno.wedding.R
import com.elno.wedding.common.Constants
import com.elno.wedding.databinding.FragmentContactBinding
import com.elno.wedding.domain.model.VendorModel
import com.elno.wedding.presentation.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactFragment : BaseFragment<FragmentContactBinding>(FragmentContactBinding::inflate), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var vendorModel: VendorModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorModel= arguments?.getParcelable(Constants.OFFER_MODEL)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupViews() {
        if(vendorModel?.location != null) {
            binding.mapView.isVisible = true
            binding.mapView.getMapAsync(this)
        }
        else {
            binding.mapView.isVisible = false
        }
        binding.wp.icon.setImageResource(R.drawable.ic_whatsapp)
        binding.call.icon.setImageResource(R.drawable.ic_phone)
        binding.instagram.icon.setImageResource(R.drawable.ic_instagram)
        binding.wp.title.text = "Whatsapp-da yaz"
        binding.call.title.text = "Zəng et"
        binding.instagram.title.text = "Instagram"
    }

    override fun setupListeners() {
        binding.wp.root.setOnClickListener {
            vendorModel?.whatsapp?.let { wp ->
                val url = "https://wa.me/${wp}"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }

        }
        binding.call.root.setOnClickListener {
            vendorModel?.mobile?.let { mobile ->
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:${mobile}")
                startActivity(dialIntent)
            }

        }

        binding.instagram.root.setOnClickListener {
            vendorModel?.instagram?.let { instagram ->
                val uri = Uri.parse("http://instagram.com/_u/$instagram")
                val likeIng = Intent(Intent.ACTION_VIEW, uri)

                likeIng.setPackage("com.instagram.android")

                try {
                    startActivity(likeIng)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/$instagram")
                        )
                    )
                }
            }

        }


    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = true
        val lat : Double = vendorModel?.location?.get("latitude") ?: 38.955734
        val lng : Double = vendorModel?.location?.get("longitude") ?: 45.629189
        val position = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(position))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}