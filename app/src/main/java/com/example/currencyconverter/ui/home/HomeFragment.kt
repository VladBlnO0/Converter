package com.example.currencyconverter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.databinding.FragmentHomeBinding
import java.util.Locale
import android.text.Editable
import android.text.TextWatcher
import com.example.currencyconverter.ui.history.HistoryModel
import com.example.currencyconverter.ui.history.HistoryViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Values for spinner
    private val currency = mapOf(
        "USD" to 1.0,
        "UAH" to 40.81,
        "EUR" to 0.91
    )

    private val valuesFirst = arrayOf("USD", "UAH", "EUR")
    private val valuesSecond = arrayOf("UAH", "USD", "EUR")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupCurrencyConverter()

        return root
    }

    private fun setupCurrencyConverter() {
        val firstSpinner: Spinner = binding.firstCurrency2
        val secondSpinner: Spinner = binding.secondCurrency2

        // First spinner getting values
        val adapterFirst = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, valuesFirst)
        adapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Second spinner getting values
        val adapterSecond = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, valuesSecond)
        adapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        firstSpinner.adapter = adapterFirst
        secondSpinner.adapter = adapterSecond

        // First spinner
        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val secondSelectedItem = secondSpinner.selectedItem.toString()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == secondSelectedItem) {
                    Toast.makeText(requireContext(), "Select another currency", Toast.LENGTH_SHORT).show()
                    firstSpinner.setSelection(0)
                    secondSpinner.setSelection(0)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Second spinner
        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val firstSelectedItem = firstSpinner.selectedItem.toString()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == firstSelectedItem) {
                    Toast.makeText(requireContext(), "Select another currency", Toast.LENGTH_SHORT).show()
                    firstSpinner.setSelection(0)
                    secondSpinner.setSelection(0)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Change spinners
        binding.changeCurrency2.setOnClickListener {
            val firstSelectedItem = firstSpinner.selectedItem.toString()
            val secondSelectedItem = secondSpinner.selectedItem.toString()
            firstSpinner.setSelection(valuesFirst.indexOf(secondSelectedItem))
            secondSpinner.setSelection(valuesSecond.indexOf(firstSelectedItem))

            if (binding.input.text.toString().toDoubleOrNull() != null) {
                when (firstSpinner.selectedItem.toString()) {
                    "USD" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "USD",
                                "UAH")
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "USD",
                                "EUR")
                        }
                    }
                    "UAH" -> {
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "UAH",
                                "USD")
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "UAH",
                                "EUR")
                        }
                    }
                    "EUR" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "EUR",
                                "UAH")
                        }
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            binding.output2.text = convertIFELSE(
                                binding.input.text.toString().toDouble(),
                                "EUR",
                                "USD")
                        }
                    }
                }
            }
        }

        // Converting
        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.output2.text = String.format(Locale.getDefault(), "Input your sum")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (binding.input.text.toString().toDoubleOrNull() != null) {
                    when (firstSpinner.selectedItem.toString()) {
                        "USD" -> {
                            if (secondSpinner.selectedItem.toString() == "UAH") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "USD",
                                    "UAH")
                            }
                            if (secondSpinner.selectedItem.toString() == "EUR") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "USD",
                                    "EUR")
                            }
                        }
                        "UAH" -> {
                            if (secondSpinner.selectedItem.toString() == "USD") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "UAH",
                                    "USD")
                            }
                            if (secondSpinner.selectedItem.toString() == "EUR") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "UAH",
                                    "EUR")
                            }
                        }
                        "EUR" -> {
                            if (secondSpinner.selectedItem.toString() == "UAH") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "EUR",
                                    "UAH")
                            }
                            if (secondSpinner.selectedItem.toString() == "USD") {
                                binding.output2.text = convertIFELSE(
                                    binding.input.text.toString().toDouble(),
                                    "EUR",
                                    "USD")
                            }
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Optional
            }
        })


        // New activity
        binding.save2.setOnClickListener {
            val viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

            if (binding.input.text.toString().toDoubleOrNull() != null && binding.input.text.toString().toDoubleOrNull() != 0.0) {
                when (firstSpinner.selectedItem.toString()) {
                    "USD" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            val resultModel = HistoryModel("USD to UAH",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "USD", "UAH")
                            )
                            viewModel.addHistory(resultModel)
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            val resultModel = HistoryModel("USD to EUR",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "USD", "EUR")
                            )
                            viewModel.addHistory(resultModel)
                        }
                    }
                    "UAH" -> {
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            val resultModel = HistoryModel("UAH to USD",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "UAH", "USD")
                            )
                            viewModel.addHistory(resultModel)
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            val resultModel = HistoryModel("UAH to EUR",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "UAH", "EUR")
                            )
                            viewModel.addHistory(resultModel)
                        }
                    }
                    "EUR" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            val resultModel = HistoryModel("EUR to UAH",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "EUR", "UAH")
                            )
                            viewModel.addHistory(resultModel)
                        }
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            val resultModel = HistoryModel("EUR to USD",
                                originalData(binding.input.text.toString().toDouble()),
                                convertIFELSE(binding.input.text.toString().toDouble(), "EUR", "USD"),
                            )
                            viewModel.addHistory(resultModel)
                        }
                    }
                }
            }
            else {
                Toast.makeText(requireContext(), "Try to write something!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun originalData(amount: Double): String {

        return String.format(Locale.getDefault(), "%.3f", amount).trimEnd('0').trimEnd('.')
    }

    private fun convertC(amount: Double, from: String, to: String): Double {
        val fromRate = currency[from]!!
        val toRate = currency[to]!!

        return amount / fromRate * toRate
    }

    private fun convertIFELSE(
        input: Double,
        fromCurrency: String,
        toCurrency: String
    ): String {
        return String.format(Locale.getDefault(),
            "%.3f",
            convertC(
            input,
            fromCurrency,
            toCurrency))
            .trimEnd('0').trimEnd('.')
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}