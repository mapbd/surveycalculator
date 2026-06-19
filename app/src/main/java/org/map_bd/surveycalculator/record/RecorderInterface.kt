package org.map_bd.surveycalculator.record

import java.io.File

interface RecorderInterface {
    fun start(outputFile: File)
    fun stop()
}