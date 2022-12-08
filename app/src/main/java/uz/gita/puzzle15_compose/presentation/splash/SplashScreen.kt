package uz.gita.puzzle15_compose.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import uz.gita.puzzle15_compose.R

// Created by Jamshid Isoqov an 11/10/2022
class SplashScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        SplashScreenContent()
    }
}

@Composable
fun SplashScreenContent() {


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.birds))
        val logoAnimationState = animateLottieCompositionAsState(composition = composition)
        LottieAnimation(
            composition = composition,
            progress = logoAnimationState.progress,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(180.dp)
                .align(Alignment.Center)
        ) {

            Image(
                painter = painterResource(id = R.drawable.btn_bg),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "Puzzle 15",
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
               fontSize = 28.sp,
                color = Color.White,
            )
        }

        Text(
            text = "Powered by Gita Academy",
            modifier = Modifier
                .padding(bottom = 18.dp)
                .wrapContentSize()
                .align(Alignment.BottomCenter),
            fontSize = 24.sp,
            color = Color.White,
        )

    }


}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreenContent()
}