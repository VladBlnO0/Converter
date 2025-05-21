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
import android.util.Log
import com.example.currencyconverter.ui.history.HistoryModel
import com.example.currencyconverter.ui.history.HistoryViewModel

import com.example.currencyconverter.ui.home.CurrencyModel
import com.example.currencyconverter.ui.home.CurrencyNamesModel

import com.example.currencyconverter.ui.home.HomeViewModel

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.currencyconverter.network.RetrofitClient

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var homeViewModel: HomeViewModel
    private var baseCurrency: String = "eur"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        historyViewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            if (item != null) {
                binding.input.setText(item.valueText.toString())
                historyViewModel.selectItem(null)
            }
        }

        setupCurrencyConverter()

        return root
    }
    private var currencyRates: Map<String, Double> = emptyMap()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUG", "onViewCreated started")
        homeViewModel.loadRates("eur")

        homeViewModel.loadCurrencyNames()

        // Rates
        homeViewModel.currencyRates.observe(viewLifecycleOwner) { rates ->
            currencyRates = rates
            Log.d("CurrencyRates", "Rates loaded: $rates")
            binding.output2.text = convert()
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.getCurrencyNames()
                val currencyList = response.map { (code, name) ->
                    "${code.uppercase()} - $name"
                }.sorted()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupCurrencyConverter() {
        val firstSpinner: Spinner = binding.firstCurrency2
        val secondSpinner: Spinner = binding.secondCurrency2


        // First spinner getting values
//        val adapterFirst = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, valuesFirst)
//        adapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Second spinner getting values
//        val adapterSecond = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, valuesSecond)
//        adapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

//        firstSpinner.adapter = adapterFirst
//        secondSpinner.adapter = adapterSecond


        homeViewModel.currencyNames.observe(viewLifecycleOwner) { names ->
            val items = names.map { (code, name) ->
                if (name.isBlank() || name == code) "${code.uppercase()} - Cryptocurrency"
                else "${code.uppercase()} - $name"
            }.sorted()

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.firstCurrency2.adapter = adapter
            binding.secondCurrency2.adapter = adapter

            binding.firstCurrency2.setSelection(items.indexOfFirst { it.startsWith("USD") })
            binding.secondCurrency2.setSelection(items.indexOfFirst { it.startsWith("UAH") })

            // First spinner
            firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val secondSelectedItem = secondSpinner.selectedItem.toString()

                    val selectedItem = parent.getItemAtPosition(position).toString()

                    if (selectedItem == secondSelectedItem) {
                        Toast.makeText(requireContext(), "Select another currency", Toast.LENGTH_SHORT).show()
                        binding.firstCurrency2.setSelection(items.indexOfFirst { it.startsWith("USD") })
                        binding.secondCurrency2.setSelection(items.indexOfFirst { it.startsWith("UAH") })
                    }
                    val fromCurrencyCode = selectedItem.substringBefore(" -").lowercase()

                    homeViewModel.loadRates(fromCurrencyCode)

                    binding.output2.text = convert()
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
                        binding.firstCurrency2.setSelection(items.indexOfFirst { it.startsWith("USD") })
                        binding.secondCurrency2.setSelection(items.indexOfFirst { it.startsWith("UAH") })
                    }

                    binding.output2.text = convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            // Change spinners
            binding.changeCurrency2.setOnClickListener {
                val firstSelectedItem = firstSpinner.selectedItem.toString()
                val secondSelectedItem = secondSpinner.selectedItem.toString()

                val newFirst = items.indexOfFirst { it.startsWith(secondSelectedItem.substringBefore(" -")) }
                val newSecond = items.indexOfFirst { it.startsWith(firstSelectedItem.substringBefore(" -")) }

                firstSpinner.setSelection(newFirst)
                secondSpinner.setSelection(newSecond)

                binding.output2.text = convert()
            }
        }

        // Converting
        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.output2.text = String.format(Locale.getDefault(), "Input your sum")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.output2.text = convert()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        // Save Result
        binding.save2.setOnClickListener {
            val viewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

            if (binding.input.text.toString().toDoubleOrNull() != null && binding.input.text.toString().toDoubleOrNull() != 0.0) {
                viewModel.addHistory(saveConvert()) {
                    Toast.makeText(requireContext(), "Already exists!", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(requireContext(), "Try to write something!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun originalData(amount: Double): String {

        return String.format(Locale.getDefault(), "%.2f", amount).trimEnd('0').trimEnd('.')
    }

    private fun convertC(amount: Double, from: String, to: String): Double {
        if (from == to) return amount

        val fromRate = if (from == "eur") 1.0 else currencyRates[from]
        val toRate = if (to == "eur") 1.0 else currencyRates[to]

        if (fromRate == null || toRate == null) {
            throw IllegalArgumentException("Missing rate for $from or $to")
        }

        return amount / fromRate * toRate
    }

    private fun convertIFELSE(
        input: Double,
        fromCurrency: String,
        toCurrency: String
    ): String {
        return String.format(Locale.getDefault(),
            "%.5f",
            convertC(
            input,
            fromCurrency,
            toCurrency))
            .trimEnd('0').trimEnd('.')
    }
    private fun convert() : String {
        val inputValue = binding.input.text.toString().toDoubleOrNull() ?: return ""

        val from = binding.firstCurrency2.selectedItem.toString().substringBefore(" -").lowercase()
        val to = binding.secondCurrency2.selectedItem.toString().substringBefore(" -").lowercase()

        Log.d("Convert", "base=$baseCurrency, from=$from, to=$to")

        if (currencyRates.isEmpty()) return "Loading rates..."

        return convertIFELSE(inputValue, from, to)
    }
    private fun saveConvert(): HistoryModel {
        val inputValue = binding.input.text.toString().toDouble()
        val from = binding.firstCurrency2.selectedItem.toString()
        val to = binding.secondCurrency2.selectedItem.toString()


        val fromText = binding.firstCurrency2.selectedItem.toString().substringBefore(" -")
        val toText = binding.secondCurrency2.selectedItem.toString().substringBefore(" -")


        val resultModel = HistoryModel(
            "$fromText to $toText",
            originalData(inputValue),
            convertIFELSE(inputValue, from.substringBefore(" -").lowercase(), to.substringBefore(" -").lowercase())
        )
        return resultModel
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}