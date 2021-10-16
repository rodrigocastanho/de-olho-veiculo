package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint
import com.google.android.gms.ads.AdRequest

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import br.com.devnattiva.deolhoveiculo.databinding.FragmentAdMobBinding
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.lang.Exception
import kotlin.jvm.Throws

class AdMobFragment : Fragment() {

    private var viewAdmobBind: FragmentAdMobBinding? = null
    private lateinit var adView: AdView
    private val adSizeConfig: AdSize get() {
        val resolucaoTela = activity?.windowManager?.defaultDisplay
        val metrica = DisplayMetrics()
        resolucaoTela?.getRealMetrics(metrica)

        val densidade = metrica.density

        var larguraPixels = viewAdmobBind!!.adView.width.toFloat()
        if(larguraPixels == 0f) {
            larguraPixels = metrica.widthPixels.toFloat()
        }

        val larguraRealTela = (larguraPixels / densidade).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity?.baseContext!!, larguraRealTela)

    }

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
            MobileAds.initialize(viewAdmobBind!!.root.context) {}

            adView = AdView(activity?.baseContext!!)
            viewAdmobBind!!.adView.addView(adView)
            bannerAd()

        }catch (e: Exception) {
            Log.e("ERRO_AD_BANNER", "ERRO_ADMOB_BANNER $e")
        }

    }

    @SuppressLint("MissingPermission")
    @Throws(Exception::class)
    private fun bannerAd() {
        adView.adUnitId = viewAdmobBind!!.adView.adUnitId
        adView.adSize = adSizeConfig

        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewAdmobBind = null
    }

}