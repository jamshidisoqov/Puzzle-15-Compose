package uz.gita.puzzle15_compose.presentation.main

import android.media.MediaPlayer
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.puzzle15_compose.R
import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.presentation.main.impl.MainViewModelImpl

// Created by Jamshid Isoqov an 11/11/2022

class MainScreen : AndroidScreen() {


    @Composable
    override fun Content() {
        val viewModel: MainViewModel = getViewModel<MainViewModelImpl>()
        val uiState = viewModel.collectAsState().value


        MainScreenContent(
            uiState = uiState,
            eventDispatcher = viewModel::onEventDispatcher
        )


    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun MainScreenContent(
        uiState: MainUiState,
        eventDispatcher: (MainIntent) -> Unit
    ) {


        val volumeState: Boolean by remember {
            mutableStateOf(uiState.volume)
        }

        val context = LocalContext.current

        val mediaPlayer: MediaPlayer by remember {
            mutableStateOf(MediaPlayer.create(context, R.raw.sound))
        }
        LifecycleEffect(
            onStarted = {
                mediaPlayer.isLooping = true
                if (volumeState)
                    mediaPlayer.start()
            },
            onDisposed = {
                mediaPlayer.pause()
            }
        )

        val selected = remember { mutableStateOf(false) }

        val selectedMedium = remember { mutableStateOf(false) }

        val selectedHard = remember { mutableStateOf(false) }

        val selectedInfo = remember { mutableStateOf(false) }

        val selectedStatistics = remember { mutableStateOf(false) }

        val selectedVolume = remember { mutableStateOf(false) }

        val scaleSelected = animateFloatAsState(if (selected.value) 0.8f else 1f)
        val scaleMedium = animateFloatAsState(if (selectedMedium.value) 0.8f else 1f)
        val scaleHard = animateFloatAsState(if (selectedHard.value) 0.8f else 1f)
        val scaleInfo= animateFloatAsState(if (selectedInfo.value) 0.8f else 1f)
        val scaleStatistics= animateFloatAsState(if (selectedStatistics.value) 0.8f else 1f)
        val scaleVolume= animateFloatAsState(if (selectedVolume.value) 0.8f else 1f)


        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .width(147.dp)
                    .height(91.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.head_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .width(147.dp)
                        .height(91.dp)
                )
                Text(
                    text = "Puzzle 15",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 18.dp, start = 8.dp),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(
                        fonts = listOf(Font(R.font.kalam_bold))
                    )
                )
            }



            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier
                    .scale(scaleSelected.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selected.value = false
                                eventDispatcher.invoke(MainIntent.OpenGameScreen(Level.EASY))
                            }
                        }
                        true
                    }
                    .padding(vertical = 8.dp)
                    .width(130.dp)
                    .height(55.dp)

                ) {
                    Image(
                        modifier = Modifier
                            .width(130.dp)
                            .height(55.dp),
                        painter = painterResource(id = R.drawable.bg_main_type),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = ""
                    )

                    Text(
                        text = "Easy",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )

                }
                Box(modifier = Modifier
                    .scale(scaleMedium.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedMedium.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selectedMedium.value = false
                                eventDispatcher.invoke(MainIntent.OpenGameScreen(Level.MEDIUM))
                            }
                        }
                        true
                    }
                    .padding(vertical = 8.dp)
                    .width(140.dp)
                    .height(60.dp)

                ) {
                    Image(
                        modifier = Modifier
                            .width(140.dp)
                            .height(58.dp),
                        painter = painterResource(id = R.drawable.bg_main_type),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = ""
                    )

                    Text(
                        text = "Medium",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )

                }




                Box(modifier = Modifier
                    .scale(scaleHard.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedHard.value = true
                            }
                            MotionEvent.ACTION_UP -> {
                                selectedHard.value = false
                                eventDispatcher.invoke(MainIntent.OpenGameScreen(Level.HARD))
                            }
                        }
                        true
                    }
                    .padding(vertical = 8.dp)
                    .width(150.dp)
                    .height(62.dp)) {
                    Image(
                        modifier = Modifier
                            .width(150.dp)
                            .height(63.dp),
                        painter = painterResource(id = R.drawable.bg_main_type),
                        contentScale = ContentScale.FillBounds,
                        contentDescription = ""
                    )

                    Text(
                        text = "Hard",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )

                }

            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(bottom = 24.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {

                Box(modifier = Modifier
                    .scale(scaleInfo.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedInfo.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selectedInfo.value = false
                                eventDispatcher.invoke(MainIntent.OpenInfoScreen)
                            }
                        }
                        true
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.num_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .height(72.dp)
                            .width(72.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.help_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(32.dp)
                            .width(32.dp)
                    )
                }

                Box(modifier = Modifier
                    .scale(scaleStatistics.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedStatistics.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selectedStatistics.value = false
                                eventDispatcher.invoke(MainIntent.OpenStatistics)
                            }
                        }
                        true
                    }

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.num_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .height(72.dp)
                            .width(72.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.lead_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(36.dp)
                            .width(36.dp)
                    )

                }

                Box(modifier = Modifier
                    .scale(scaleVolume.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedVolume.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selectedVolume.value = false
                                eventDispatcher.invoke(MainIntent.VolumeClick)
                            }
                        }
                        true
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.num_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .height(72.dp)
                            .width(72.dp)
                    )

                    if (uiState.volume) {
                        mediaPlayer.start()
                    } else {
                        mediaPlayer.pause()
                    }

                    Image(
                        painter = painterResource(
                            id = if (uiState.volume) R.drawable.volume_on_bg
                            else R.drawable.volume_off_bg
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(32.dp)
                            .width(32.dp)
                    )
                }
            }
        }
    }
    @Preview
    @Composable
    fun MainScreenPreview() {
        MainScreenContent(uiState = MainUiState(), eventDispatcher ={})
    }
}

