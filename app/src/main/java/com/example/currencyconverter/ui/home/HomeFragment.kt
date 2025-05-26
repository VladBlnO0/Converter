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
import com.example.currencyconverter.db.HistoryEntity
import kotlinx.coroutines.launch
import com.example.currencyconverter.network.RetrofitClient
import com.example.currencyconverter.ui.history.HistoryDbViewModel

class HomeFragment : Fragment() {
    private lateinit var dbViewModel: HistoryDbViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var homeViewModel: HomeViewModel
    private var baseCurrency: String = "eur"
    private var currencyItems: List<String> = emptyList()

    private var restoringFromHistory = false
    private var pendingRestore: HistoryModel? = null


    private val cur = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        historyViewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]
        historyViewModel.selectedItem.observe(viewLifecycleOwner) { item ->
            pendingRestore = item
        }

        setupCurrencyConverter()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("DEBUG", "onViewCreated started")

        if (!cur) {
            homeViewModel.loadRates("eur")
            homeViewModel.loadCurrencyNames()
            cur
        }

        dbViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(HistoryDbViewModel::class.java)

        dbViewModel.allHistory.observe(viewLifecycleOwner) { list ->
            val newHistory = HistoryEntity(
                fromCurrency = "USD",
                toCurrency = "UAH",
                inputValue = "100",
                resultValue = "4300"
            )
            dbViewModel.insert(newHistory)
        }
    }

    private fun setupCurrencyConverter() {
        val firstSpinner: Spinner = binding.firstCurrency2
        val secondSpinner: Spinner = binding.secondCurrency2

        homeViewModel.currencyNames.observe(viewLifecycleOwner) { names ->
            val items = names.map { (code, name) ->
                if (name.isBlank() || name == code) "${code.uppercase()} - Cryptocurrency"
                else "${code.uppercase()} - $name"
            }.sorted()

            currencyItems = items

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            val lastFrom = homeViewModel.selectedFromCurrency.value ?: "USD"
            val lastTo = homeViewModel.selectedToCurrency.value ?: "UAH"

            firstSpinner.adapter = adapter
            secondSpinner.adapter = adapter

//            if (isFirstLaunch) {
//                binding.firstCurrency2.setSelection(currencyItems.indexOfFirst { it.startsWith("USD") })
//                binding.secondCurrency2.setSelection(currencyItems.indexOfFirst { it.startsWith("UAH") })
//                isFirstLaunch = false
//            }


            val fromIndex = currencyItems.indexOfFirst { it.substringBefore(" -") == lastFrom }
            val toIndex = currencyItems.indexOfFirst { it.substringBefore(" -") == lastTo }
            if (fromIndex >= 0) firstSpinner.setSelection(fromIndex)
            if (toIndex >= 0) secondSpinner.setSelection(toIndex)

            pendingRestore?.let { binding.input.setText(it.valueText) }
            pendingRestore = null

            historyViewModel.selectItem(null)


//
//            if (!restoringFromHistory && currencyItems.isNotEmpty() && firstSpinner.selectedItem == null) {
//                firstSpinner.setSelection(currencyItems.indexOfFirst { it.startsWith("USD") })
//                secondSpinner.setSelection(currencyItems.indexOfFirst { it.startsWith("UAH") })
//            }


            // First spinner
            firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (restoringFromHistory) {

                        val currencyCode = currencyItems[position].substringBefore(" -")
                        homeViewModel.selectedFromCurrency.value = currencyCode

                        binding.output2.text = convert()

                        return
                    }
                    binding.output2.text = convert()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            // Second spinner
            secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    if (restoringFromHistory) {

                        val currencyCode = currencyItems[position].substringBefore(" -")
                        homeViewModel.selectedToCurrency.value = currencyCode

                        binding.output2.text = convert()

                        return
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

                if (newFirst >= 0 && newSecond >= 0) {
                    firstSpinner.setSelection(newFirst)
                    secondSpinner.setSelection(newSecond)
                }

                binding.output2.text = convert()
            }
        }

        // Converting
        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.output2.text = String.format(Locale.getDefault(), "Input your sum")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.firstCurrency2.selectedItem != null && binding.secondCurrency2.selectedItem != null) {
                    binding.output2.text = convert()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        // Save Result
        binding.save2.setOnClickListener {
            if (binding.input.text.toString().toDoubleOrNull() != null && binding.input.text.toString().toDoubleOrNull() != 0.0) {
                historyViewModel.addHistory(saveConvert()) {
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
        val rates = homeViewModel.currencyRates.value ?: return 1.0

        if (from == to) return amount

        val fromRate = if (from == "eur") 1.0 else rates[from]
        val toRate = if (to == "eur") 1.0 else rates[to]

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

        val fromRaw = binding.firstCurrency2.selectedItem
        val toRaw = binding.secondCurrency2.selectedItem

        val from = fromRaw.toString().substringBefore(" -").lowercase()
        val to = toRaw.toString().substringBefore(" -").lowercase()

        Log.d("Convert", "base=$baseCurrency, from=$from, to=$to")

//        if (rates.isEmpty()) return "Loading rates..."

        return convertIFELSE(inputValue, from, to)
    }
    private fun saveConvert(): HistoryModel {
        val inputValue = binding.input.text.toString().toDouble()

        val fromText = binding.firstCurrency2.selectedItem.toString().substringBefore(" -")
        val toText = binding.secondCurrency2.selectedItem.toString().substringBefore(" -")

        val resultModel = HistoryModel(
            fromCurrencyCode = fromText,
            toCurrencyCode = toText,
            valueText = originalData(inputValue),
            convertedValueText = convertIFELSE(inputValue, fromText.lowercase(), toText.lowercase())
        )

        return resultModel
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}