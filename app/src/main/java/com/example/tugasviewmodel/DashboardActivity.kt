package com.example.tugasviewmodel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class DashboardActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra("EXTRA_USERNAME") ?: "Guest"
        viewModel.username.value = username

        setContent {
            DashboardScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val context = LocalContext.current
    val username by viewModel.username
    val items = viewModel.items

    val addItemLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val itemName = data?.getStringExtra("EXTRA_ITEM_NAME")
            val itemQuantity = data?.getStringExtra("EXTRA_ITEM_QUANTITY")

            if (itemName != null && itemQuantity != null) {
                viewModel.addItem(itemName, itemQuantity)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dashboard") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddItemActivity::class.java)
                addItemLauncher.launch(intent)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Barang")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Selamat Datang, $username",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Daftar Barang Anda:", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Text("Nama Barang", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                Text("Banyaknya", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }

            Divider()

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.name, modifier = Modifier.weight(2f))
                        Text(item.quantity, modifier = Modifier.weight(1f))
                    }
                    Divider()
                }
            }
        }
    }
}