package ua.vitolex.yesorno

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chaintech.network.cmpshakedetection.rememberShakeDetector
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.vitolex.yesorno.ui.theme.YesOrNoTheme
import kotlin.random.Random
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import ua.vitolex.yesorno.ui.BannerAdView
import ua.vitolex.yesorno.ui.theme.gardu


class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private final val TAG = "MainActivity"
    val count = mutableStateOf(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        loadAd()
        setContent {
//            viewModel = hiltViewModel()
            val shakeDetector = rememberShakeDetector()
            var refreshing by remember { mutableStateOf(false) }
            var answer by remember { mutableStateOf(Random.nextInt(0, 2)) }
//            var count by remember { mutableStateOf(0) }

            var background by remember { mutableStateOf(Color.Black) }
            var textVisible by remember { mutableStateOf(true) }

            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.Asset("animation.json")
            )
            var animSpec = LottieClipSpec.Progress(
                0f,
                0.5705f
            )


            // Start detecting shakes
            LaunchedEffect(Unit) {
                shakeDetector.start()
            }

            shakeDetector.onShake {
                if (!refreshing) {
                    refreshing = true
                    count.value ++
//                    if (count.value % 3 == 0) mInterstitialAd?.show(this@MainActivity)

                        // Perform your refresh action here
                        MainScope().launch {
                            textVisible = false
                            delay(1000L)
                            answer = Random.nextInt(0, 2)
                            delay(600L)
                            textVisible = true
                            delay(2000) // Simulate a refresh delay
                            refreshing = false
                            if (count.value % 3 == 0) mInterstitialAd?.show(this@MainActivity)
                    }

                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    shakeDetector.stop()
                }
            }

//            mInterstitialAd?.show(this@MainActivity)

            YesOrNoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(background),
                    color = background
                ) {
                    LazyColumn (modifier = Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween ){
                        item {
                            Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
//                            BannerAdView(id = "ca-app-pub-1869740172940843/9807122771")
                                BannerAdView(id = "ca-app-pub-3940256099942544/9214589741")
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        item {
                            Box(modifier = Modifier
                                .fillMaxWidth(),
//                                .weight(0.3f)
//                                .background(Color.Green),
                                contentAlignment = Alignment.Center){
                                Text(
                                    text = "Your answer",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 26.sp,
                                    fontFamily = gardu,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        item {
                            Box(modifier = Modifier
                                .height(300.dp)
                                .width(300.dp),
//                                .weight(1f)
//                                .background(Color.Blue),
                                contentAlignment = Alignment.Center ){
                                LottieAnimation(
                                    composition = composition,
                                    isPlaying = true,
                                    iterations = 100,
                                    reverseOnRepeat = true,
                                    speed = 1.2f,
                                    clipSpec = animSpec,
//                                modifier =Modifier.scale(0.8f),
                                )
                                Box(modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .padding(top = 18.dp),
                                    contentAlignment = Alignment.Center){
                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = textVisible,
                                        enter = scaleIn(animationSpec = tween(600)),
                                        exit = fadeOut(animationSpec = tween(600)),
                                    ){
                                        Text(
                                            color = Color.Red,
                                            fontFamily = gardu,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 74.sp,
                                            text = if (answer == 1) "YES" else "NO"
                                        )
                                    }

                                }
                            }
                        }

item {
    Spacer(modifier = Modifier.height(10.dp))
    Box(modifier = Modifier
        .fillMaxWidth(),
//        .weight(0.2f)
//        .background(Color.White),
        contentAlignment = Alignment.Center){
        Text(
            text = "Shake your phone",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            fontFamily = gardu,
            color = Color.Red
        )
    }
}
                       item {
                           Spacer(modifier = Modifier.height(10.dp))
                           Box(modifier = Modifier.heightIn(min = 60.dp), contentAlignment = Alignment.Center) {
//                            BannerAdView(id = "ca-app-pub-1869740172940843/9807122771")
                               BannerAdView(id = "ca-app-pub-3940256099942544/9214589741")
                           }
                       }

                    }

                }
            }
        }

    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onAdFailedToLoad(adError: LoadAdError) {
                adError.toString().let { Log.d(TAG, it) }
                mInterstitialAd = null
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
                mInterstitialAd?.fullScreenContentCallback = adListener()
            }
        })
    }

    private fun adListener() = object : FullScreenContentCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
//            count.value = 1

            Log.d(TAG, "Ad dismissed fullscreen content.")
            mInterstitialAd = null
            loadAd()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            // Called when ad fails to show.
            Log.e(TAG, "Ad failed to show fullscreen content.")
            mInterstitialAd = null
            loadAd()
        }
    }
}
