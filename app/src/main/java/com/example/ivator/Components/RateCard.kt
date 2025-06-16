package com.example.ivator.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivator.ui.theme.dblue
import com.example.ivator.ui.theme.lblue
import com.example.ivator.ui.theme.urbanistFontFamily

@Composable
fun RateCard(
    cardUnit: String,
    cardHeading: String,
    liveDropRate: String,
    displayedDripRate: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
            .padding(20.dp)
    ) {
        Text(
            text = cardHeading,
            fontSize = 25.sp,
            fontWeight = FontWeight(1000),
            color = dblue,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = liveDropRate,
            fontSize = 40.sp,
            fontWeight = FontWeight(800),
            color = lblue,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
        )
        Text(
            text = cardUnit,
            fontSize = 25.sp,
            fontWeight = FontWeight(600),
            color = lblue,
            modifier = Modifier
                .padding(horizontal = 5.dp)
        )
        Text(
            text = "set rate : $displayedDripRate",
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
            color = dblue,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            text = "change",
            fontSize = 18.sp,
            fontWeight = FontWeight(800),
            textDecoration = TextDecoration.Underline,
            color = dblue,
            modifier = Modifier.padding(5.dp)
        )
    }
}
