package org.map_bd.surveycalculator.play

import java.io.File

interface AudioInterface {
    fun playFile(file: File)
    fun stop()
}