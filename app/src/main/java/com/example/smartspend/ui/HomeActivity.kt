package com.example.smartspend.ui

import Expense
import ExpenseAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.R
import com.example.smartspend.ui.addexpense.AddExpenseActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvTotalSpent = findViewById<TextView>(R.id.tvTotalSpent)
        val rvExpenses = findViewById<RecyclerView>(R.id.rvExpenses)
        val fabAddExpense = findViewById<FloatingActionButton>(R.id.fabAddExpense)

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name")
                    tvGreeting.text = if (!name.isNullOrEmpty()) "Hello, $name!" else "Hello!"
                }
                .addOnFailureListener {
                    tvGreeting.text = "Hello!"
                }
        } else {
            tvGreeting.text = "Hello!"
        }

        expenseAdapter = ExpenseAdapter(expenses)
        rvExpenses.adapter = expenseAdapter
        rvExpenses.layoutManager = LinearLayoutManager(this)

        FirestoreHelper.getExpenses { list ->
            expenses.clear()
            expenses.addAll(list)
            expenseAdapter.notifyDataSetChanged()
            val total = expenses.sumOf { it.amount }
            tvTotalSpent.text = "Total Spent: Rs. $total"
        }

        fabAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
    }
}
