package org.map_bd.surveycalculator


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityAccurateLandCalculationBinding
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import org.map_bd.surveycalculator.EstateAssets
import org.map_bd.surveycalculator.HeirsInput
import org.map_bd.surveycalculator.InheritanceViewModel
import org.map_bd.surveycalculator.PdfExportState
import kotlin.getValue


class AccurateLandCalculationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccurateLandCalculationBinding
    private val viewModel: InheritanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccurateLandCalculationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEventObservers()
        setupActionListeners()
    }

    private fun setupEventObservers() {
        viewModel.distributionResults.observe(this) { results ->
            if (results.isNotEmpty()) {
                binding.btnExportPdf.visibility = View.VISIBLE
            }
        }

        viewModel.currentLanguage.observe(this) { lang ->
            binding.btnToggleLanguage.text = if (lang == "bn") "English" else "বাংলা"
        }

        viewModel.pdfState.observe(this) { state ->
            when (state) {
                is PdfExportState.Loading -> { }
                is PdfExportState.Success -> {
                    Toast.makeText(this, "Saved: ${state.file.absolutePath}", Toast.LENGTH_LONG).show()
                    viewModel.resetPdfState()
                }
                is PdfExportState.Error -> {
                    Toast.makeText(this, "Export Error: ${state.exception.message}", Toast.LENGTH_SHORT).show()
                    viewModel.resetPdfState()
                }
                else -> {}
            }
        }
    }

    private fun setupActionListeners() {
        binding.btnToggleLanguage.setOnClickListener { viewModel.toggleLanguage() }

        binding.btnCalculate.setOnClickListener {
            val heirs = HeirsInput(
                husbands = binding.etHusband.text.toString().toIntOrNull() ?: 0,
                wives = binding.etWife.text.toString().toIntOrNull() ?: 0,
                fathers = binding.etFather.text.toString().toIntOrNull() ?: 0,
                mothers = binding.etMother.text.toString().toIntOrNull() ?: 0,
                sons = binding.etSons.text.toString().toIntOrNull() ?: 0,
                daughters = binding.etDaughters.text.toString().toIntOrNull() ?: 0
            )
            val assets = EstateAssets(
                landShatansh = binding.etAssetLand.text.toString().toDoubleOrNull() ?: 0.0,
                goldBhori = binding.etAssetGold.text.toString().toDoubleOrNull() ?: 0.0,
                silverBhori = binding.etAssetSilver.text.toString().toDoubleOrNull() ?: 0.0,
                cashTaka = binding.etAssetCash.text.toString().toDoubleOrNull() ?: 0.0
            )
            viewModel.processComputation(heirs, assets)
        }

        binding.btnExportPdf.setOnClickListener { viewModel.executePdfGeneration(this) }
    }
}
