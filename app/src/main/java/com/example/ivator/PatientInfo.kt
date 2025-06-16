    package com.example.ivator

    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.width
    import androidx.compose.material3.Button
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavController


    @Composable
    fun Patientinfo(navController: NavController
        ) {

        var name by remember {
           mutableStateOf("")
        }
        var age by remember {
            mutableStateOf("")
        }
        var liquid by remember {
            mutableStateOf("")
        }

        var gender by remember {
            mutableStateOf("")
        }

        var driprate by remember {
            mutableStateOf("")
        }

        var flowrate by remember {
            mutableStateOf("")
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
                Text(
                    text = "Enter Patient's Detail",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight(500)
                )
            Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(value = name, onValueChange = {
                                                                name = it
                }, label = {
                    Text(text = "Patient's Name")
                })

            Spacer(modifier = Modifier.height(10.dp))
                 OutlinedTextField(value = age, onValueChange = {
                                                                age = it
                 }, label = {
                Text(text = "Age")
                })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = gender, onValueChange = {
                                                              gender = it
            }, label = {
                Text(text = "Gender")
            })
            Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = liquid, onValueChange = {
                                                                  liquid = it
                }, label = {
                Text(text = "Liquid Attached")
                })

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = flowrate, onValueChange = {
                flowrate = it
            }, label = {
                Text(text = "Flow Rate")
            })

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(value = driprate, onValueChange = {
                driprate = it
            }, label = {
                Text(text = "Drip Rate")
            })

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                            navController.navigate("home/$name/$age/$gender/$liquid/$flowrate/$driprate") },
                modifier = Modifier.width(280.dp)){
                Text(text = "Submit");
           }
        }
    }