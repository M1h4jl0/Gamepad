package com.controller.desktopapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.onClick
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import controllerapp.composeapp.generated.resources.button
import controllerapp.composeapp.generated.resources.greenLedOff
import controllerapp.composeapp.generated.resources.greenLedON
import controllerapp.composeapp.generated.resources.redLedOff
import controllerapp.composeapp.generated.resources.redLedON
import controllerapp.composeapp.generated.resources.switchOn
import controllerapp.composeapp.generated.resources.switchOff
import controllerapp.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import  jssc.SerialPortList
import jssc.SerialPort
import jssc.SerialPortException
import jssc.SerialPortEvent
import jssc.SerialPortEventListener
import kotlinx.coroutines.CoroutineScope


@Composable
@Preview
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        var buttonStates by remember { mutableStateOf(List(5) {false}) }
        fun animateButtonPress(index: Int, scope: CoroutineScope) {
            scope.launch {
                buttonStates = buttonStates.toMutableList().also {
                    it[index] = true
                }
                delay(100L)
                buttonStates = buttonStates.toMutableList().also {
                    it[index] = false
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF4D674D))
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ) {
                items(5) { index ->
                    val isClicked = buttonStates[index]
                    val yOffset = if (isClicked) 10.dp else 0.dp

                    Image(
                        painter = painterResource(Res.drawable.button),
                        contentDescription = "Button ${index + 1}",
                        modifier = Modifier
                            .offset(y = yOffset)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                animateButtonPress(index, coroutineScope)
                            }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                var isSwitched by remember { mutableStateOf(false) }
                var isSwitched2 by remember { mutableStateOf(false) }

                var yOffset = 0.dp
                var image = Res.drawable.switchOn
                var greenLedImage = Res.drawable.greenLedOff
                var greenLedOffset = 0.dp

                var yOffset2 = 0.dp
                var image2 = Res.drawable.switchOn
                var redLedImage = Res.drawable.redLedOff
                var redLedOffset = 0.dp

                if(isSwitched) {
                    image = Res.drawable.switchOn
                    yOffset = -10.dp
                    greenLedImage = Res.drawable.greenLedON
                    greenLedOffset = 1.dp
                } else {
                    image = Res.drawable.switchOff
                    yOffset = 5.dp
                    greenLedImage = Res.drawable.greenLedOff
                    greenLedOffset = 0.dp
                }

                if(isSwitched2) {
                    image2 = Res.drawable.switchOn
                    yOffset2 = -10.dp
                    redLedImage = Res.drawable.redLedON
                    redLedOffset = 1.dp
                } else {
                    image2 = Res.drawable.switchOff
                    yOffset2 = 5.dp
                    redLedImage = Res.drawable.redLedOff
                    redLedOffset = 0.dp
                }

                Row(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(50.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.Bottom
                ){
                    Image(
                        painter = painterResource(greenLedImage),
                        contentDescription = "Green Led Off",
                        modifier = Modifier.offset(y = greenLedOffset)
                    )
                    Image(
                        painter = painterResource(redLedImage),
                        contentDescription = "Red Led Off",
                        modifier = Modifier.offset(y = redLedOffset)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(60.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(image),
                        contentDescription = "Switch 1",
                        modifier = Modifier.offset(y = yOffset).clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){
                            isSwitched = !isSwitched
                        }
                    )
                    Image(
                        painter = painterResource(image2),
                        contentDescription = "Switch 2",
                        modifier = Modifier.offset(y = yOffset2).clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){
                            isSwitched2 = !isSwitched2
                        }
                    )
                }
            }
        }
        val cS = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            startSerialListener { index ->
                animateButtonPress(index, cS)
            }
        }

    }
}

fun startSerialListener(onButtonDetected: (Int) -> Unit) {
    val port = SerialPort("COM8")
    try {
        port.openPort()
        port.setParams(
            SerialPort.BAUDRATE_9600,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE
        )

        port.addEventListener(object : SerialPortEventListener {
            override fun serialEvent(event: SerialPortEvent) {
                if (event.isRXCHAR) {
                    val data = port.readString(event.eventValue)?.trim()
                    when (data) {
                        "B1" -> onButtonDetected(4)
                        "B2" -> onButtonDetected(3)
                        "B3" -> onButtonDetected(2)
                        "B4" -> onButtonDetected(1)
                        "B5" -> onButtonDetected(0)
                    }
                }
            }
        })

    } catch (e: SerialPortException) {
        
        println("Serial error: ${e.message}")
    }
}


