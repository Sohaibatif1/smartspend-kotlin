import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartspend.R
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private val categoryIcons = mapOf(
        "Food" to "🍔",
        "Shopping" to "🛒",
        "Transport" to "🚗",
        "Entertainment" to "🎬",
        "Bills" to "💡",
        "Other" to "💸"
    )

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryIcon: TextView = itemView.findViewById(R.id.tvCategoryIcon)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.tvCategory.text = expense.category
        holder.tvNote.text = expense.note
        holder.tvAmount.text = "Rs. ${expense.amount}"
        holder.tvDate.text = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(expense.date))


        holder.tvCategoryIcon.text = categoryIcons[expense.category] ?: "💸"
    }

    override fun getItemCount(): Int = expenses.size
}
