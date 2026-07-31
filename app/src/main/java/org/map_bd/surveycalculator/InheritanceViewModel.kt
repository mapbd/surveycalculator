package org.map_bd.surveycalculator

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

sealed class PdfExportState {
    object Idle : PdfExportState()
    object Loading : PdfExportState()
    data class Success(val file: File) : PdfExportState()
    data class Error(val exception: Throwable) : PdfExportState()
}

class InheritanceViewModel(application: Application) : AndroidViewModel(application) {

    private val calculator = InheritanceCalculator()

    private val _distributionResults = MutableLiveData<List<HeirResult>>()
    val distributionResults: LiveData<List<HeirResult>> get() = _distributionResults

    private val _pdfState = MutableLiveData<PdfExportState>(PdfExportState.Idle)
    val pdfState: LiveData<PdfExportState> get() = _pdfState

    private val _currentLanguage = MutableLiveData<String>("bn")
    val currentLanguage: LiveData<String> get() = _currentLanguage

    private var lastComputedHeirs: HeirsInput = HeirsInput()
    private var lastComputedAssets: EstateAssets = EstateAssets()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "bn") "en" else "bn"
        processComputation(lastComputedHeirs, lastComputedAssets)
    }

    fun processComputation(heirs: HeirsInput, assets: EstateAssets) {
        lastComputedHeirs = heirs
        lastComputedAssets = assets

        viewModelScope.launch(Dispatchers.Default) {
            val results = calculator.calculateDistribution(heirs, assets)
            _distributionResults.postValue(results)
        }
    }

    fun executePdfGeneration(context: Context) {
        val currentResults = _distributionResults.value ?: return
        val currentLang = _currentLanguage.value ?: "bn"

        _pdfState.value = PdfExportState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val targetFile = calculator.exportToPdf(context, currentResults, lastComputedAssets, currentLang)
                withContext(Dispatchers.Main) {
                    if (targetFile != null && targetFile.exists()) {
                        _pdfState.value = PdfExportState.Success(targetFile)
                    } else {
                        _pdfState.value = PdfExportState.Error(RuntimeException("Failed to stream binary data write to PDF system storage."))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _pdfState.value = PdfExportState.Error(e)
                }
            }
        }
    }
    
    fun resetPdfState() {
        _pdfState.value = PdfExportState.Idle
    }
}
