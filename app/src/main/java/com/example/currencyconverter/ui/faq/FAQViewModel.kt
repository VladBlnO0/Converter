package com.example.currencyconverter.ui.faq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FAQViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is FAQ Fragment"
    }
    val text: LiveData<String> = _text
}