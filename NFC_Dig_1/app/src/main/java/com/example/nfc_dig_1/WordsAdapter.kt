import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

data class WordItem(val id: Int, val word: String, var isChosen: Boolean = false)


class WordsAdapter(private val context: Context, private val words: List<WordItem>) : BaseAdapter() {
    private var selectedItemId: Int? = null

    override fun getCount(): Int {
        return words.size
    }

    override fun getItem(position: Int): Any {
        return words[position]
    }

    override fun getItemId(position: Int): Long {
        return words[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val textView: TextView = view.findViewById(android.R.id.text1)
        val wordItem = words[position]
        textView.text = wordItem.word

        // Update the background color based on the selection state
        if (wordItem.id == selectedItemId) {
            view.setBackgroundColor(Color.LTGRAY)
        } else {
            view.setBackgroundColor(Color.TRANSPARENT)
        }

        return view
    }

    fun selectItemById(id: Int) {
        selectedItemId = id
        notifyDataSetChanged()
    }
}
