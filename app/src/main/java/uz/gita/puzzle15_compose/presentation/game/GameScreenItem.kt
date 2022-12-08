package uz.gita.puzzle15_compose.presentation.game

import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import uz.gita.puzzle15_compose.R

// Created by Jamshid Isoqov an 11/11/2022


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameNumberItem(text: String, modifier: Modifier, click: (Int) -> Unit) {

    val selected = remember { mutableStateOf(false) }
    val scale = animateFloatAsState(if (selected.value) 0.8f else 1f)

    Box(modifier = modifier
        .scale(scale.value)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    selected.value = true
                }

                MotionEvent.ACTION_UP -> {
                    selected.value = false
                    click.invoke(text.toInt())
                }
            }
            true
        }) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.num_bg),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            fontSize = 18.sp,
            color = Color.White,
            fontFamily = FontFamily(
                listOf(Font(R.font.kalam_bold))
            )
        )
    }

}

@Composable
fun GameNumberItemNoVisible(modifier: Modifier) {
    Box(modifier = modifier) {

    }
}

@Preview
@Composable
fun GameNumberItemPreview() {
    Column(modifier = Modifier.size(320.dp)) {
        var s = 0
        for (i in 0..2) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                for (j in 0..2) {
                    GameNumberItem(
                        text = "${s++}",
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxHeight()
                            .weight(1f)
                    ) {

                    }
                }
            }
        }
    }
}