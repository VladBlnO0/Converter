package com.example.currencyconverter

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.databinding.ActivityMainBinding

import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import java.util.Locale
import android.text.Editable
import android.text.TextWatcher

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //Values for spinner
    private val currency = mapOf<String, Double>(
    "USD" to 1.0,
    "UAH" to 40.81,
    "EUR" to 0.91
    )

    private val valuesFirst = arrayOf("USD", "UAH", "EUR")
    private val valuesSecond = arrayOf("UAH", "USD", "EUR")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val firstSpinner: Spinner = binding.appBarMain.firstCurrency
        val secondSpinner: Spinner = binding.appBarMain.secondCurrency

        //First spinner getting values
        val adapterFirst = ArrayAdapter(this, android.R.layout.simple_spinner_item, valuesFirst)
        adapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Second spinner getting values
        val adapterSecond = ArrayAdapter(this, android.R.layout.simple_spinner_item, valuesSecond)
        adapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        firstSpinner.adapter = adapterFirst
        secondSpinner.adapter = adapterSecond

        //First spinner
        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val secondSelectedItem = secondSpinner.selectedItem.toString()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == secondSelectedItem) {
                    Toast.makeText(this@MainActivity, "Select another currency", Toast.LENGTH_SHORT).show()
                    firstSpinner.setSelection(0)
                    secondSpinner.setSelection(0)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Second spinner
        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val firstSelectedItem = firstSpinner.selectedItem.toString()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == firstSelectedItem) {
                    Toast.makeText(this@MainActivity, "Select another currency", Toast.LENGTH_SHORT).show()
                    firstSpinner.setSelection(0)
                    secondSpinner.setSelection(0)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Change spinners
        binding.appBarMain.changeCurrency.setOnClickListener { view ->
            val firstSelectedItem = firstSpinner.selectedItem.toString()
            val secondSelectedItem = secondSpinner.selectedItem.toString()
            firstSpinner.setSelection(valuesFirst.indexOf(secondSelectedItem))
            secondSpinner.setSelection(valuesSecond.indexOf(firstSelectedItem))

            val input = binding.appBarMain.input.text.toString().toDoubleOrNull()

            if (input != null) {
                when (firstSpinner.selectedItem.toString()) {
                    "USD" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "USD", "UAH")
                            ).trimEnd('0').trimEnd('.')
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "USD", "EUR")
                            ).trimEnd('0').trimEnd('.')
                        }
                    }

                    "UAH" -> {
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "UAH", "USD")
                            ).trimEnd('0').trimEnd('.')
                        }
                        if (secondSpinner.selectedItem.toString() == "EUR") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "UAH", "EUR")
                            ).trimEnd('0').trimEnd('.')
                        }
                    }

                    "EUR" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "EUR", "UAH")
                            ).trimEnd('0').trimEnd('.')
                        }
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            binding.appBarMain.output.text = String.format(
                                Locale.getDefault(),
                                "%.3f",
                                convertC(input, "EUR", "USD")
                            ).trimEnd('0').trimEnd('.')
                        }
                    }
                }
            }
        }

        //Converting
        binding.appBarMain.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.appBarMain.output.text = String.format(Locale.getDefault(),"Input your sum")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = binding.appBarMain.input.text.toString().toDoubleOrNull()

                if (input != null) {
                    when (firstSpinner.selectedItem.toString()) {
                        "USD" -> {
                            if (secondSpinner.selectedItem.toString() == "UAH") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "USD", "UAH")
                                ).trimEnd('0').trimEnd('.')
                            }
                            if (secondSpinner.selectedItem.toString() == "EUR") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "USD", "EUR")
                                ).trimEnd('0').trimEnd('.')
                            }
                        }

                        "UAH" -> {
                            if (secondSpinner.selectedItem.toString() == "USD") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "UAH", "USD")
                                ).trimEnd('0').trimEnd('.')
                            }
                            if (secondSpinner.selectedItem.toString() == "EUR") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "UAH", "EUR")
                                ).trimEnd('0').trimEnd('.')
                            }
                        }

                        "EUR" -> {
                            if (secondSpinner.selectedItem.toString() == "UAH") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "EUR", "UAH")
                                ).trimEnd('0').trimEnd('.')
                            }
                            if (secondSpinner.selectedItem.toString() == "USD") {
                                binding.appBarMain.output.text = String.format(
                                    Locale.getDefault(),
                                    "%.3f",
                                    convertC(input, "EUR", "USD")
                                ).trimEnd('0').trimEnd('.')
                            }
                        }
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // Optional
            }
        })

        //New activity
        binding.appBarMain.save.setOnClickListener { view ->
            val intent = Intent(this, SaveActivity::class.java)
            startActivity(intent)
            // TODO: add here saving of converting
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_faq
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    fun convertC(amount: Double, from: String, to: String): Double {
        val fromRate = currency[from]!!
        val toRate = currency[to]!!

        return amount / fromRate * toRate
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}