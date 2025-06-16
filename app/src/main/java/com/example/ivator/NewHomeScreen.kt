package com.example.ivator


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ivator.Components.RateCard
import com.example.ivator.ui.theme.dblue
import com.example.ivator.ui.theme.dongleFontFamily
import com.example.ivator.ui.theme.lblue
import com.example.ivator.ui.theme.urbanistFontFamily
import com.example.ivator.ui.theme.white


@Composable
@Preview(showSystemUi = true, showBackground = true)
fun NewHomeScreen(){

    val gender = "M"
    val age = "55"
    val liquid = "glucol"
    val name = "Johny"


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.group1),
                contentDescription = null
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "IVATOR",
                    fontSize = 60.sp,
                    color = white,
                    fontFamily = dongleFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 0.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        Text(
                            text = "$name",
                            fontSize = 25.sp,
                            color = white,
                            fontFamily = urbanistFontFamily, fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 2.dp)
                        )
                        Text(
                            text = "$age | $gender | $liquid",
                            fontSize = 15.sp,
                            fontFamily = urbanistFontFamily, fontWeight = FontWeight(400),
                            modifier = Modifier.padding(top = 1.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            RateCard(
                cardHeading = "Drip Rate",
                cardUnit = "drops/min",
                liveDropRate = "24",
                displayedDripRate = "30"
            )

            RateCard(
                cardHeading = "Flow Rate",
                cardUnit = "ml/min",
                liveDropRate = "24",
                displayedDripRate = "30"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 25.dp)
                .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
                .background(
                    color = Color(0xFfffffff),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Liquometer",
                    fontSize = 25.sp,
                    fontFamily = urbanistFontFamily,
                    fontWeight = FontWeight(1000),
                    color = dblue,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Current Status",
                        fontFamily = urbanistFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(600),
                        color = dblue,
                    )
                    Text(
                        text = "--/ 500ml",
                        fontFamily = urbanistFontFamily,
                        fontSize = 25.sp,
                        color = lblue,
                        fontWeight = FontWeight(800),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}


