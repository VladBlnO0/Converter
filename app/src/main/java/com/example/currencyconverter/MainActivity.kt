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
    private var valuesFirst = arrayOf("USD", "UAH")
    private var valuesSecond = arrayOf("UAH", "USD")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val firstSpinner: Spinner = binding.appBarMain.firstCurrency;
        val secondSpinner: Spinner = binding.appBarMain.secondCurrency;

        val adapterFirst = ArrayAdapter(this, android.R.layout.simple_spinner_item, valuesFirst)
        adapterFirst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapterSecond = ArrayAdapter(this, android.R.layout.simple_spinner_item, valuesSecond)
        adapterSecond.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        firstSpinner.adapter = adapterFirst
        secondSpinner.adapter = adapterSecond

        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        };
        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val firstSelectedItem = firstSpinner.selectedItem.toString()
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == firstSelectedItem) {
                    Toast.makeText(this@MainActivity, "NOOOO", Toast.LENGTH_SHORT).show()
                    secondSpinner.setSelection(0)
                }
                else {
                    Toast.makeText(this@MainActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        };

        binding.appBarMain.convertbutton.setOnClickListener { view ->
            val input = binding.appBarMain.input.text.toString()
            binding.appBarMain.output.text = input
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