package com.example.tugasviewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class Item(val name: String, val quantity: String)

class DashboardViewModel : ViewModel() {

    val username = mutableStateOf("")

    val items = mutableStateListOf<Item>()

    fun addItem(name: String, quantity: String) {
        if (name.isNotBlank() && quantity.isNotBlank()) {
            items.add(Item(name, quantity))
        }
    }
}