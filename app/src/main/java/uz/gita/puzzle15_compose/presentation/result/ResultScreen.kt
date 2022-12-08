package uz.gita.puzzle15_compose.presentation.result

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import org.orbitmvi.orbit.compose.collectAsState
import uz.gita.puzzle15_compose.R
import uz.gita.puzzle15_compose.utils.getTime

// Created by Jamshid Isoqov an 11/17/2022
class ResultScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel: ResultViewModel = getViewModel<ResultViewModelImpl>()
        val uiState = viewModel.collectAsState().value
        ResultScreenContent(uiState, viewModel::onEventDispatcher)
        BackHandler {
            viewModel.onEventDispatcher(ResultIntent.Back)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResultScreenContent(uiState: ResultUiState, block: (ResultIntent) -> Unit) {

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
                text = "Result",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 8.dp),
                color = Color.White,
                fontSize = 23.sp,
                fontFamily = FontFamily(
                    fonts = listOf(Font(R.font.kalam_bold))
                )
            )
        }

        Box(
            modifier = Modifier
                .width(350.dp)
                .height(450.dp)
                .align(Alignment.Center)
        ) {

            Image(
                painter = painterResource(id = R.drawable.best),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {

                    Text(
                        text = "Best records",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fonts = listOf(Font(R.font.kalam_bold))),
                        modifier = Modifier
                            .padding(top = 28.dp)
                            .align(Alignment.Center)
                    )

                }

                val list = uiState.resultList

                LazyColumn(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {
                    items(list.size) {
                        Text(
                            text = "${it + 1}. Time ${list[it].time.getTime()}  Moved : ${list[it].move}",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(fonts = listOf(Font(R.font.kalam_bold))),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 8.dp)
                        )
                    }
                }
            }
        }

        val selected = remember { mutableStateOf(false) }
        val scale = animateFloatAsState(if (selected.value) 0.8f else 1f)


        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 24.dp)
            .scale(scale.value)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        selected.value = true
                    }

                    MotionEvent.ACTION_UP -> {
                        selected.value = false
                        block.invoke(ResultIntent.Back)
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


@Preview
@Composable
fun ResultScreenPreview() {

    ResultScreenContent(ResultUiState()) {

    }

}
