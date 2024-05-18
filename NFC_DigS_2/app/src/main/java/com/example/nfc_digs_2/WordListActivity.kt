package com.example.nfc_digs_2

import WordsAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.core.view.children
import com.example.nfc_digs_2.databinding.ActivityWordListBinding
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class WordListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//      var receivedWord = intent.getStringExtra("receivedWord")
        var receivedWord = "test"
        var receivedPublicKey = ""
        var randomWords = (getRandomWords(9) + receivedWord).shuffled()
        val adapter = WordsAdapter(this, randomWords)
        binding.wordList.adapter = adapter
        highlightCorrectWord(receivedWord, randomWords)

    }
    private fun getRandomWords(numWords: Int): List<String> {
        val wordPool = listOf(
            "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honeydew",
            "kiwi", "lemon", "mango", "nectarine", "orange", "papaya", "quince", "raspberry",
            "strawberry", "tangerine", "ugli", "vanilla", "watermelon", "xigua", "yellowfruit", "zucchini",
            "avocado", "blackberry", "blueberry", "cantaloupe", "cranberry", "dragonfruit", "gooseberry",
            "guava", "jackfruit", "kumquat", "lime", "lychee", "mandarin", "mulberry", "olive", "peach",
            "pear", "persimmon", "pineapple", "plum", "pomegranate", "pumpkin", "rambutan", "soursop",
            "starfruit", "tamarind", "tomato", "yuzu", "apricot", "bilberry", "boysenberry", "clementine",
            "damson", "feijoa", "jambul", "longan", "loquat", "medlar", "nashi", "passionfruit", "pawpaw",
            "plantain", "prune", "satsuma", "sloe", "tangelo", "tayberry", "ugni", "whortleberry",
            "chikoo", "durian", "elderflower", "grapefruit", "huckleberry", "jabuticaba", "kiwano", "lakoocha",
            "mammee", "mangosteen", "marionberry", "muscadine", "naranjilla", "nance", "pitanga", "rambai",
            "salak", "santol", "sapodilla", "serviceberry", "surinam cherry", "wax apple", "white currant"
        )

        return wordPool.shuffled().take(numWords)
    }
    fun verifySignature(data: ByteArray, signatureBytes: ByteArray, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        signature.update(data)
        return signature.verify(signatureBytes)
    }
    fun highlightCorrectWord(receivedWord:String, randomWords: List<String>){
        var index = randomWords.indexOf(receivedWord)
        println(binding.wordList.children.toList().toString())
        binding.wordList.getChildAt(index).setBackgroundColor(Color.Green.hashCode())
    }
//    fun bytesToPublicKey(bytes: ByteArray): PublicKey {
//        val keySpec = X509EncodedKeySpec(bytes)
//        val keyFactory = KeyFactory.getInstance("RSA")
//        return keyFactory.generatePublic(keySpec)
//    }
//
//    // Function to convert a byte array back to a PrivateKey
//    fun bytesToPrivateKey(bytes: ByteArray): PrivateKey {
//        val keySpec = PKCS8EncodedKeySpec(bytes)
//        val keyFactory = KeyFactory.getInstance("RSA")
//        return keyFactory.generatePrivate(keySpec)
//    }
}