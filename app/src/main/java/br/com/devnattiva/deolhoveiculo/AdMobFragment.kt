package br.com.devnattiva.deolhoveiculo

import android.os.Build

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import br.com.devnattiva.deolhoveiculo.databinding.FragmentAdMobBinding
import com.google.android.gms.ads.*

class AdMobFragment : Fragment() {

    private var _viewAdmobBind: FragmentAdMobBinding? = null
    private val viewAdmobBind get() = _viewAdmobBind!!
    private lateinit var adView: AdView
    private var initLayout = false

    private val adSize: AdSize
        get() { return adsApativeConfig() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _viewAdmobBind = FragmentAdMobBinding.inflate(inflater, container, false)
        return viewAdmobBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext()) {}
        bannerAd()
    }

    private fun bannerAd() {
        adView = AdView(requireContext())
        adView.setAdSize(adSize)
        viewAdmobBind.adView.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initLayout) {
                initLayout = true
                val adRequest = AdRequest.Builder().build()
                viewAdmobBind.adView.loadAd(adRequest)
                viewAdmobBind.adView.adListener = object: AdListener() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("ADS_DEBUG", adError.toString())
                    }
                }
            }
        }
    }

    private fun adsApativeConfig(): AdSize {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val bounds = windowMetrics.bounds

            var adWidthPixels = viewAdmobBind.adView.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()
            AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(requireContext(), adWidth)
        } else {
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), 320)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewAdmobBind = null
    }
}