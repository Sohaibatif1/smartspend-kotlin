package com.example.smartspend.ui.addexpense

import Expense
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspend.R
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var spCategory: Spinner
    private lateinit var etNote: EditText
    private lateinit var btnAddExpense: Button


    private var selectedDateMillis: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        etAmount = findViewById(R.id.etAmount)
        spCategory = findViewById(R.id.spCategory)
        etNote = findViewById(R.id.etNote)
        btnAddExpense = findViewById(R.id.btnAddExpense)

        val categories = listOf("Food", "Shopping", "Transport", "Entertainment", "Bills", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter


        etNote.setOnLongClickListener {
            showDatePicker()
            true
        }

        btnAddExpense.setOnClickListener {
            val amountText = etAmount.text.toString().trim()
            val category = spCategory.selectedItem as String
            val note = etNote.text.toString().trim()
            val amount = amountText.toDoubleOrNull()

            if (amount == null || amount <= 0) {
                etAmount.error = "Enter a valid amount"
                return@setOnClickListener
            }

            val expense = Expense(
                amount = amount,
                category = category,
                note = note,
                date = selectedDateMillis
            )

            FirestoreHelper.addExpense(expense) { success ->
                if (success) {
                    Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show()
                    finish() // Close activity, go back to Home
                } else {
                    Toast.makeText(this, "Error adding expense", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMillis
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDateMillis = calendar.timeInMillis
                Toast.makeText(this, "Date selected!", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
