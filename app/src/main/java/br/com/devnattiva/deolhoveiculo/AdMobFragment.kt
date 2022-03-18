package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import br.com.devnattiva.deolhoveiculo.databinding.FragmentAdMobBinding
import com.google.android.gms.ads.*
import java.lang.Exception
import kotlin.jvm.Throws

class AdMobFragment : Fragment() {

    private var viewAdmobBind: FragmentAdMobBinding? = null
    private lateinit var adView: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewAdmobBind = FragmentAdMobBinding.inflate(inflater, container, false)
        return viewAdmobBind!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            MobileAds.initialize(requireContext()) {}
            bannerAd()

        }catch (e: Exception) {
            Log.e("ERRO_AD_BANNER", "ERRO_ADMOB_BANNER $e")
        }

    }

    @SuppressLint("MissingPermission")
    @Throws(Exception::class)
    private fun bannerAd() {
        adView = AdView(requireContext())
        val adRequest = AdRequest.Builder().build()
        viewAdmobBind?.adView!!.loadAd(adRequest)
        viewAdmobBind?.adView!!.adListener = object: AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("ADS_DEBUG", adError.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewAdmobBind = null
    }

}