package uz.gita.puzzle15_compose.presentation.game

import android.media.MediaPlayer
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
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
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.hilt.getViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import uz.gita.puzzle15_compose.R
import uz.gita.puzzle15_compose.data.enums.Level
import uz.gita.puzzle15_compose.presentation.game.impl.GameViewModelImpl
import uz.gita.puzzle15_compose.ui.theme.App_Color
import uz.gita.puzzle15_compose.utils.getTime
import kotlin.math.sqrt

// Created by Jamshid Isoqov an 11/11/2022
class GameScreen(private val level: Level) : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: GameViewModel = getViewModel<GameViewModelImpl>()

        LaunchedEffect(key1 = level) {
            viewModel.generateOrGet(level)
        }

        var openWinState: Boolean by remember {
            mutableStateOf(false)
        }

        viewModel.collectSideEffect {
            openWinState = it.openWinGame
        }


        BackHandler {

            viewModel.onEventDispatcher(GameIntent.Back)

        }

        val uiState = viewModel.collectAsState().value

        var winDialog: Pair<Int, Int> by remember {
            mutableStateOf(Pair(0, 0))
        }

        if (winDialog.first != 0) {
            OpenWinDialog(time = winDialog.first, move = winDialog.second, {
                viewModel.playingNewGame()
            }) {
                viewModel.onEventDispatcher(GameIntent.Back)
            }
        }

        GameScreenContent(
            level = level,
            uiState = uiState,
            eventDispatcher = viewModel::onEventDispatcher,
            {
                viewModel.saveTime(it)
            }) { time, moved ->

            winDialog = Pair(time, moved)


        }
        DisposableEffect(key1 = viewModel) {
            onDispose {
                viewModel.saveGame()
            }
        }
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun GameScreenContent(
        level: Level,
        uiState: GameUiState,
        eventDispatcher: (GameIntent) -> Unit,
        finish: (Int) -> Unit,
        win: (Int, Int) -> Unit
    ) {

        var time: Int by remember { mutableStateOf(0) }

        var isTimer:Boolean by remember {
            mutableStateOf(true)
        }


        LaunchedEffect(key1 = uiState.time) {
            time = uiState.time
            while (isTimer) {
                delay(1000)
                time++
            }
        }

        if (uiState.isWin){
            if (time>0) {
                isTimer = false
                win.invoke(time, uiState.moved)
                eventDispatcher.invoke(GameIntent.SaveGame(time = time, moved = 0))
            }
        }
        /*LaunchedEffect(key1 = uiState.isWin) {

        }*/


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
                finish.invoke(time)
                mediaPlayer.pause()
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(75.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.head_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .width(180.dp)
                            .height(110.dp)
                    )
                    Text(
                        text = time.getTime(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp, start = 8.dp),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(75.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.head_bg),
                        contentDescription = "",
                        modifier = Modifier
                            .width(180.dp)
                            .height(110.dp)
                    )
                    Text(
                        text = uiState.moved.toString(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp, start = 8.dp),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )
                }
            }


            Column(
                modifier = Modifier
                    .size(340.dp)
                    .align(Alignment.Center)
            ) {
                val list = uiState.numberList
                val size = sqrt(list.size.toDouble()).toInt()
                for (i in 0 until size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (j in 0 until size) {
                            val coordinate = i * level.level + j
                            if (uiState.emptyCoordinate.first == j && uiState.emptyCoordinate.second == i) {
                                GameNumberItemNoVisible(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxHeight()
                                        .weight(1f)
                                )
                            } else {
                                GameNumberItem(
                                    text = "${list[coordinate]}",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxHeight()
                                        .weight(1f)
                                ) {
                                    eventDispatcher.invoke(GameIntent.NumberClick(Pair(j, i)))
                                }
                            }
                        }
                    }
                }
            }

            val selected = remember { mutableStateOf(false) }
            val scale = animateFloatAsState(if (selected.value) 0.8f else 1f)


            Box(
                modifier = Modifier
                    .scale(scale.value)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected.value = true
                            }

                            MotionEvent.ACTION_UP -> {
                                selected.value = false
                                time = 0
                                eventDispatcher.invoke(GameIntent.Refresh)
                            }
                        }
                        true
                    }
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 132.dp, height = 98.dp)
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.refresh_bg),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp)

                )
            }

        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OpenWinDialog(time: Int, move: Int, refresh: () -> Unit, close: () -> Unit) {

    var showDialog by remember { mutableStateOf(true) }


    val selected = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (selected.value) 0.8f else 1f)


    val selectedClose = remember { mutableStateOf(false) }
    val scaleClose = animateFloatAsState(if (selectedClose.value) 0.8f else 1f)


    if (showDialog) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .background(
                        color = App_Color,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(24.dp)
                ) {

                    Text(
                        text = "Congratulation",
                        modifier = Modifier
                            .align(CenterHorizontally),
                        fontSize = 24.sp,
                        color = Color.White,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.trophy),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .size(120.dp)
                            .align(CenterHorizontally)
                    )
                    Text(
                        text = "Time  ${time.getTime()}",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Move : $move",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontFamily = FontFamily(
                            fonts = listOf(Font(R.font.kalam_bold))
                        ),
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                        Box(modifier = Modifier
                            .scale(scale.value)
                            .pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        selected.value = true
                                    }

                                    MotionEvent.ACTION_UP -> {
                                        selected.value = false
                                        showDialog = false
                                        refresh.invoke()
                                    }
                                }
                                true
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.num_bg),
                                contentDescription = "",
                                modifier = Modifier.size(72.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.refresh_bg),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(36.dp)
                                    .width(36.dp)
                            )

                        }

                        Box(modifier = Modifier
                            .scale(scaleClose.value)
                            .pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        selectedClose.value = true
                                    }

                                    MotionEvent.ACTION_UP -> {
                                        selectedClose.value = false
                                        showDialog = false
                                        close.invoke()
                                    }
                                }
                                true
                            }) {
                            Image(
                                painter = painterResource(id = R.drawable.num_bg),
                                contentDescription = "",
                                modifier = Modifier.size(72.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(36.dp)
                                    .width(36.dp)
                            )

                        }

                    }

                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GameScreenPreview() {
    OpenWinDialog(time = 0, move = 0,{}) {

    }
}
