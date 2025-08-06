package com.example.smartspend.data

import Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreHelper {

    fun addExpense(expense: Expense, onComplete: (Boolean) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onComplete(false)
            return
        }

        val ref = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("expenses")
            .document() // create new document with random ID

        ref.set(expense)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getExpenses(onResult: (List<Expense>) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            onResult(emptyList())
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("expenses")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val expenses = snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
                onResult(expenses)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
}
