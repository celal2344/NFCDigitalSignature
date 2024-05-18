import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class WordsAdapter(context: Context, private val words: List<String?>) :
    ArrayAdapter<String>(context, 0, words) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        val wordTextView = itemView?.findViewById<TextView>(android.R.id.text1)
        wordTextView?.text = getItem(position)

        return itemView!!
    }

    override fun getItem(position: Int): String? {
        return super.getItem(position)
    }
}
