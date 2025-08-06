import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    private fun userDoc() = db.collection("users").document(FirebaseAuth.getInstance().uid!!)

    fun addExpense(expense: Expense, onResult: (Boolean) -> Unit) {
        userDoc().collection("expenses")
            .add(expense)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getExpenses(onResult: (List<Expense>) -> Unit) {
        userDoc().collection("expenses")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null && value != null) {
                    val list = value.documents.mapNotNull { it.toObject(Expense::class.java)?.copy(id = it.id) }
                    onResult(list)
                }
            }
    }
}
