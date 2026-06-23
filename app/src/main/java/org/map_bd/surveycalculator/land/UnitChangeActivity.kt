package org.map_bd.surveycalculator.land

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.map_bd.surveycalculator.ui.unittheme.UnitConverterTheme




class UnitChangeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        UnitConverter()
                    }
                }
            }
        }
    }
}




@Composable
fun UnitConverter() {
    var input by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Kilogrammes") }
    var output by remember { mutableStateOf("") }
    var outputUnit by remember { mutableStateOf("Livre") }
    var inputUnitDropdownExpanded by remember { mutableStateOf(false) }
    var outputUnitDropdownExpanded by remember { mutableStateOf(false) }


    fun convert (): Double {
        val inputValue = input.toDoubleOrNull() ?: 0.0
        val result = when (inputUnit) {
            "Kilogrammes" ->
                when (outputUnit) {
                    "Kilogrammes" -> inputValue * 1.0
                    "Livre" -> inputValue * 2.20462
                    "Grammes" -> inputValue * 1000.0
                    else -> 0.0
                }
            "Livre" -> when (outputUnit) {
                "Kilogrammes" -> inputValue * 0.453592
                "Livre" -> inputValue * 1.0
                "Grammes" -> inputValue * 453.592
                else -> 0.0
            }
            "Grammes" -> when (outputUnit) {
                "Kilogrammes" -> inputValue * 0.001
                "Livre" -> inputValue * 0.00220462
                "Grammes" -> inputValue * 1.0
                else -> 0.0
            }
            else -> 0.0
        }
        return result
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().
        padding(top = 50.dp)
    ) {
        Text(
            text = "Unit Conversion",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                output = convert().toString()
            },
            placeholder = { Text("Entrez une valeur") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Box(){
                Button(
                    onClick = {
                        inputUnitDropdownExpanded = true
                    },
                    Modifier.width(180.dp)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = inputUnit)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                }
                DropdownMenu(
                    expanded = inputUnitDropdownExpanded,
                    onDismissRequest = {
                        inputUnitDropdownExpanded = false
                    },
                    modifier = Modifier.width(180.dp)
                ) {
                    DropdownMenuItem(text = { Text("Kilogrammes") }, onClick = {
                        inputUnit = "Kilogrammes"
                        inputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                    DropdownMenuItem(text = { Text("Livre") }, onClick = {
                        inputUnit = "Livre"
                        inputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                    DropdownMenuItem(text = { Text("Grammes") }, onClick = {
                        inputUnit = "Grammes"
                        inputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(){
                Button(
                    onClick = {
                        outputUnitDropdownExpanded = true
                    },
                    Modifier.width(180.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = outputUnit)
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
                DropdownMenu(
                    expanded = outputUnitDropdownExpanded,
                    onDismissRequest = {
                        outputUnitDropdownExpanded = false
                    },
                    modifier = Modifier.width(180.dp)

                ) {
                    DropdownMenuItem(text = { Text("Kilogrammes") }, onClick = {
                        outputUnit = "Kilogrammes"
                        outputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                    DropdownMenuItem(text = { Text("Livre") }, onClick = {
                        outputUnit = "Livre"
                        outputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                    DropdownMenuItem(text = { Text("Grammes") }, onClick = {
                        outputUnit = "Grammes"
                        outputUnitDropdownExpanded = false
                        output = convert().toString()
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Results : ${(if (output.isNotEmpty()) output else "0.0")} $outputUnit",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )



    }

}
