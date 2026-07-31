package org.map_bd.surveycalculator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

data class HeirsInput(
    val husbands: Int = 0,
    val wives: Int = 0,
    val sons: Int = 0,
    val daughters: Int = 0,
    val fathers: Int = 0,
    val mothers: Int = 0,
    val fullBrothers: Int = 0,
    val fullSisters: Int = 0,
    val consanguineBrothers: Int = 0,
    val consanguineSisters: Int = 0,
    val uterineBrothers: Int = 0,
    val uterineSisters: Int = 0
)

data class EstateAssets(
    val landShatansh: Double = 0.0,
    val goldBhori: Double = 0.0,
    val silverBhori: Double = 0.0,
    val cashTaka: Double = 0.0
)

data class HeirResult(
    val nameKey: String,
    val count: Int,
    val shareFraction: Double,
    val landAllocated: Double,
    val goldAllocated: Double,
    val silverAllocated: Double,
    val cashAllocated: Double
)

class InheritanceCalculator {

    fun calculateDistribution(heirs: HeirsInput, assets: EstateAssets): List<HeirResult> {
        val shares = mutableMapOf<String, Double>()
        val hasChildren = heirs.sons > 0 || heirs.daughters > 0
        val totalSiblings = heirs.fullBrothers + heirs.fullSisters + heirs.consanguineBrothers + 
                             heirs.consanguineSisters + heirs.uterineBrothers + heirs.uterineSisters

        if (heirs.husbands > 0) shares["husband"] = if (hasChildren) 0.25 else 0.50
        if (heirs.wives > 0) shares["wife"] = if (hasChildren) 0.125 else 0.25
        
        if (heirs.mothers > 0) {
            shares["mother"] = when {
                hasChildren || totalSiblings >= 2 -> 1.0 / 6.0
                else -> 1.0 / 3.0
            }
        }

        var fatherIsAsaba = false
        if (heirs.fathers > 0) {
            if (heirs.sons > 0) {
                shares["father"] = 1.0 / 6.0
            } else {
                shares["father"] = 1.0 / 6.0
                fatherIsAsaba = true
            }
        }

        if (heirs.daughters > 0 && heirs.sons == 0) {
            shares["daughter"] = if (heirs.daughters == 1) 0.50 else (2.0 / 3.0)
        }

        var totalAssignedShare = shares.values.sum()
        if (totalAssignedShare > 1.0) {
            for (key in shares.keys) {
                shares[key] = shares[key]!! / totalAssignedShare
            }
            totalAssignedShare = 1.0
        }

        val remainingEstate = 1.0 - totalAssignedShare
        val asabaShares = mutableMapOf<String, Double>()

        if (remainingEstate > 0.0) {
            if (heirs.sons > 0) {
                val totalUnits = (heirs.sons * 2) + heirs.daughters
                val unitValue = remainingEstate / totalUnits
                asabaShares["son"] = unitValue * 2 * heirs.sons
                if (heirs.daughters > 0) asabaShares["daughter"] = (asabaShares["daughter"] ?: 0.0) + (unitValue * heirs.daughters)
            } else if (fatherIsAsaba) {
                shares["father"] = (shares["father"] ?: 0.0) + remainingEstate
            }
        }

        val finalShares = shares.toMutableMap()
        for ((key, value) in asabaShares) {
            finalShares[key] = (finalShares[key] ?: 0.0) + value
        }

        return finalShares.map { (key, share) ->
            val count = when(key) {
                "husband" -> heirs.husbands
                "wife" -> heirs.wives
                "son" -> heirs.sons
                "daughter" -> heirs.daughters
                "father" -> heirs.fathers
                else -> heirs.mothers
            }
            HeirResult(
                nameKey = "heir_$key", count = count, shareFraction = share,
                landAllocated = assets.landShatansh * share, goldAllocated = assets.goldBhori * share,
                silverAllocated = assets.silverBhori * share, cashAllocated = assets.cashTaka * share
            )
        }.filter { it.count > 0 && it.shareFraction > 0.0 }
    }

    fun exportToPdf(context: Context, results: List<HeirResult>, assets: EstateAssets, language: String): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.textSize = 16f
        paint.isFakeBoldText = true
        canvas.drawText(if (language == "bn") "উত্তরাধিকার সম্পত্তি বন্টন ফলাফল" else "Inheritance Report", 50f, 50f, paint)
        
        pdfDocument.finishPage(page)
        val targetFile = File(Environment.getExternalStorageDirectory(), "Survey Calculator/land/Distribution_Report.pdf")
        return try {
            pdfDocument.writeTo(FileOutputStream(targetFile))
            pdfDocument.close()
            targetFile
        } catch (e: IOException) {
            pdfDocument.close()
            null
        }
    }
}
