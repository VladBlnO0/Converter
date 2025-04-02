package com.example.currencyconverter

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

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    //Values for spinner
    private val valuesFirst = arrayOf("USD", "UAH")
    private val valuesSecond = arrayOf("UAH", "USD")

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

        //Get data from spinners
        binding.appBarMain.changeCurrency.setOnClickListener { view ->
            val firstSelectedItem = firstSpinner.selectedItem.toString()
            val secondSelectedItem = secondSpinner.selectedItem.toString()
            firstSpinner.setSelection(valuesFirst.indexOf(secondSelectedItem))
            secondSpinner.setSelection(valuesSecond.indexOf(firstSelectedItem))
        }

        //Converting
        binding.appBarMain.convertbutton.setOnClickListener { view ->
            var input = binding.appBarMain.input.text.toString()
            val forMath = input.toDoubleOrNull()
            if (forMath != null) {
                when (firstSpinner.selectedItem.toString()) {
                    "USD" -> {
                        if (secondSpinner.selectedItem.toString() == "UAH") {
                            val result = forMath * 40
                            binding.appBarMain.output.text = result.toString()
                        }
                    }
                    "UAH" -> {
                        if (secondSpinner.selectedItem.toString() == "USD") {
                            val result = forMath / 40
                            binding.appBarMain.output.text = result.toString()
                        }
                    }
                }
            }
            else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
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