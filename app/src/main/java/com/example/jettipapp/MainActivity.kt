package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.utils.calculateTipAmount
import com.example.jettipapp.utils.calculateTotalPerPerson
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column(modifier = Modifier.padding(all = 12.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    MainContent()

                }
            }

        }
    }
}

//@Preview
@Composable
fun TopHeader(totalPerPerson : Double = 134.0){
    Surface(modifier = Modifier
        .padding(15.dp)
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
        ,
        elevation = 5.dp,
        color = Color(0xFFD7E5F7),
        border = BorderStroke(0.5.dp, Color.White)
    ) {
        Column(modifier = Modifier.padding(all = 12.dp),
            verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Total Per Person",
             style = MaterialTheme.typography.h5)
            val total = "%.2f".format(totalPerPerson)
        Text(text = "$$total",
             style = MaterialTheme.typography.h4,
             fontWeight = FontWeight.ExtraBold)
        }

    }
}

@Composable
fun MyApp(content : @Composable () -> Unit){
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content()

        }
    }

}


//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            Text(text = "Hello Again")
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){

    val numberOfPersons = remember {
        mutableStateOf(1)
    }

    val totalTipAmount = remember {
        mutableStateOf(0.0)
    }

    val totalBillPerPerson = remember {
        mutableStateOf(0.0)
    }

    BillForm(numberOfPersons = numberOfPersons,
        totalTipAmount = totalTipAmount,
        totalBillPerPerson = totalBillPerPerson){ billAmt ->
        Log.d("BillAmt", "Bill Form: $billAmt")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(modifier: Modifier = Modifier,
             numberOfPersons : MutableState<Int>,
             totalTipAmount : MutableState<Double>,
             totalBillPerPerson : MutableState<Double>,
             valChange : (String) -> Unit = {}){


    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()

//    val numberOfPersons = remember {
//        mutableStateOf(2)
//    }
//
//    val totalTipAmount = remember {
//        mutableStateOf(0.0)
//    }
//
//    val totalBillPerPerson = remember {
//        mutableStateOf(0.0)
//    }

    val keyboardController = LocalSoftwareKeyboardController.current

    TopHeader(totalBillPerPerson.value)

    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(6.dp),
        Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
//            Text(text = "Hello Again....")
//            Text(text = "Hello Again....")
//            Text(text = "Hello Again....")
            InputField(valueState = totalBillState ,
                labelId = "Enter Bill" ,
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if (!validState)return@KeyboardActions
                    valChange(totalBillState.value)
                    keyboardController?.hide()
                }
            )

            //0xF56C27B0
           if (validState) {

                Row(modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split", modifier = Modifier
                        .align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {

                        RoundIconButton(imageVector = Icons.Default.Remove,
                            onClick = {
                                if (numberOfPersons.value <= 1){
                                    numberOfPersons.value = 1
                                }else{
                                    //ChangeNumber(numberOfPersons.value){
                                    numberOfPersons.value --
                                    //}
                                }

                                totalBillPerPerson.value = calculateTotalPerPerson(totalBillState.value.toDouble(), tipPercentage, numberOfPersons.value)

                                Log.d("Icon", "BillForm: Removed ")})

                        Text(text = "${numberOfPersons.value}",
                        modifier = Modifier
                            .padding(start = 9.dp, end = 9.dp)
                            .align(alignment = Alignment.CenterVertically))

                        RoundIconButton(imageVector = Icons.Default.Add,
                            onClick = {
                                //ChangeNumber(numberOfPersons.value){
                                numberOfPersons.value ++
                                //}
                                totalBillPerPerson.value = calculateTotalPerPerson(totalBillState.value.toDouble(), tipPercentage, numberOfPersons.value)

                                Log.d("Icon", "BillForm: Added ")})
                    }

                }
            }else{
                Box() {

                }
            }

            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                Text(text = "Tip", modifier = Modifier
                    .align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                Text(text = "$ ${totalTipAmount.value}", modifier = Modifier
                    .align(alignment = CenterVertically))

            }

            Column(verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$tipPercentage%")
                Spacer(modifier = Modifier.width(12.dp))
                Slider(modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    value = sliderPositionState.value, onValueChange = { newVal ->
                    sliderPositionState.value = newVal
                        totalTipAmount.value = calculateTipAmount(totalBillState.value.toDouble(),
                                                tipPercentage)
                    totalBillPerPerson.value = calculateTotalPerPerson(totalBillState.value.toDouble(), tipPercentage, numberOfPersons.value)
                    Log.d("Slider", "BillForm: ${sliderPositionState.value} ")

                },
                steps = 5,
                onValueChangeFinished = {
                    Log.d("Slider", "BillForm: Finished")
                })

            }


        }
    }



}



