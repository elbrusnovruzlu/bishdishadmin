package com.elno.bishdish.presentation.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.elno.bishdish.R
import com.elno.bishdish.databinding.FragmentContactUsBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>(FragmentContactUsBinding::inflate) {

    override fun setupViews() {
        binding.facebook.icon.setImageResource(R.drawable.ic_facebook)
        binding.instagram.icon.setImageResource(R.drawable.ic_instagram)
        binding.mobile.icon.setImageResource(R.drawable.ic_phone)
        binding.email.icon.setImageResource(R.drawable.ic_mail)

        binding.facebook.title.text = getString(R.string.facebook)
        binding.instagram.title.text = getString(R.string.instagram)
        binding.mobile.subTitle.text = getString(R.string.phone_number)
        binding.email.subTitle.text = getString(R.string.email)
        binding.mobile.subTitle.isVisible = true
        binding.email.subTitle.isVisible = true
        binding.mobile.title.text = PHONE_NUMBER
        binding.email.title.text = EMAIL
    }

    override fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.instagram.root.setOnClickListener {
            openInstagram()
        }
        binding.mobile.root.setOnClickListener {
            dialPhoneNumber()
        }
        binding.email.root.setOnClickListener {
            composeEmail()
        }
        binding.facebook.root.setOnClickListener {
            openFacebook()
        }
    }

    private fun openInstagram() {
        val uri = Uri.parse("http://instagram.com/_u/${INSTAGRAM_ID}")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/${INSTAGRAM_ID}")
                )
            )
        }
    }

    private fun dialPhoneNumber() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:${PHONE_NUMBER}")
        startActivity(dialIntent)
    }

    private fun composeEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            val subject = getString(R.string.dayz_support)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            data = Uri.parse("mailto:${EMAIL}?subject=$subject")
        }
        startActivity(emailIntent)
    }

    private fun openFacebook() {
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        facebookIntent.data = Uri.parse(FACEBOOK_URL)
        startActivity(facebookIntent)
    }


    companion object {
       const val FACEBOOK_URL = "https://www.facebook.com/elbrusn"
       const val PHONE_NUMBER = "+994502709397"
       const val EMAIL = "elbrus.n95@gmail.com"
       const val INSTAGRAM_ID = "elbrusnovruzlu"
    }

}