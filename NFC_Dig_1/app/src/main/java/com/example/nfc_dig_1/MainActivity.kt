package com.example.nfc_dig_1

import WordItem
import WordsAdapter
import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.example.nfc_dig_1.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.KeyPairGenerator
import java.security.KeyPair
import java.security.PrivateKey
import java.security.Signature

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedItem: WordItem = WordItem(-1,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isButtonClickable(false)
        var words = getRandomWords(50)
        var adapter = WordsAdapter(this, words)
        binding.wordList.adapter = adapter
        var plainText = ""
        binding.wordList.setOnItemClickListener{_, _, position, _ ->
            selectedItem = words[position]
            plainText = words[position].word
            if(selectedItem.isChosen){
                resetHighlights(words)
                isButtonClickable(false)
            }else{
                highlightChosenItems(words, selectedItem.id)
                isButtonClickable(true)
            }
        }

        binding.button.setOnClickListener{
            if(plainText != "") {
                binding.button.isActivated = true
                val mdString = hashString(plainText)
                val mdStringByteArray = mdString.toByteArray()
                KeyStore.keyPair = generateKeyPair()
                val signature = signData(mdStringByteArray, KeyStore.keyPair!!.private)
//                val publicKeyBytes = KeyStore.keyPair!!.public.encoded
                //create a string with list
                var stringList = plainText
                words.shuffled().take(9).forEach() {element ->
                    stringList += " ${element.word}"
                }

                val i = Intent(this@MainActivity, NFCActivity1::class.java).apply {
                    putExtra("stringList", stringList)
                    putExtra("signature", signature)
                }

                startActivity(i)
            }else{
                Toast.makeText(this, "Please choose a word",Toast.LENGTH_LONG).show()
            }

        }
    }
    private fun highlightChosenItems(words: List<WordItem>, index: Int){
        resetHighlights(words)
        words[index].isChosen = true
        binding.wordList.getChildAt(index - binding.wordList.firstVisiblePosition).setBackgroundColor(Color.LightGray.hashCode())

    }
    private fun resetHighlights(words: List<WordItem>){
        words.forEach { element ->
            element.isChosen = false
        }
        binding.wordList.forEach {element ->
            element.setBackgroundColor(Color.Transparent.hashCode())

        }
    }
    private fun getRandomWords(numWords: Int): List<WordItem> {
        val wordPool = listOf(
            "Prescribe", "Gullible", "Start", "Neat", "Kindhearted", "Mysterious", "Fortunate", "Dim", "Broad", "Recondite",
            "Eel", "Thank", "Clinic", "Help", "Icy", "Rain", "Preference", "Fraud", "Ankh", "Meal", "Spy", "Solicit",
            "Hilarious", "Elastic", "Haunting", "Hangar", "Homesick", "Glimmer", "Decide", "Gravitational", "Approve", "Contempt",
            "Adornment", "Roof", "Gang", "Ingest", "Rastle", "Gaudy", "Crucifier", "Economics", "Nation", "Barnyard", "Prosecute",
            "Bloodsport", "Acrobatic", "Know", "Safety", "Debauchery", "Understand", "Bump", "Cement", "Portrait", "Tremor", "Explain",
            "Pavement", "Enemies", "Amazingly", "Killjoy", "Heritage", "Detachable", "Comment", "Secretive", "Blowgun", "Fatal",
            "Earthborn", "Adaptable", "Shop", "Audience", "Dropping", "Pollution", "Authority", "Shaggy", "Honey", "West", "Emotion",
            "Bauble", "Debug", "Fugitive", "Sneaky", "Hive", "Illegal", "Candy", "Robber", "Steak", "Division", "Crayon", "Innocent",
            "Bags", "Hoopla", "Scatter", "Hyaena", "Obsession", "Knowledge", "Vest", "Overjoyed", "Comrade", "Amoeba", "Intruder",
            "Friendless", "Apply", "Horrors", "Offer", "Resource", "Coma", "Exchange", "Everyone", "Cows", "Sanctify", "Ballet"
        )
        val list = mutableListOf<WordItem>()
        wordPool.shuffled().take(numWords).forEachIndexed() {index, element ->
            list += WordItem(index,element)
        }
        return list
    }
    private fun isButtonClickable(bool:Boolean){
        binding.button.isActivated = bool
        binding.button.isClickable = bool
        if(!bool){
            binding.button.setTextColor(Color.White.hashCode())
            binding.button.setBackgroundColor(Color.DarkGray.hashCode())
        }
        else{
            binding.button.setTextColor(Color.Black.hashCode())
            binding.button.setBackgroundColor(Color.Magenta.hashCode())
        }


    }
    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    private fun generateKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(2048) // You can choose a key size
        return keyGen.genKeyPair()
    }
    fun signData(data: ByteArray, privateKey: PrivateKey): ByteArray {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        signature.update(data)
        return signature.sign()
    }
    object KeyStore {
        var keyPair: KeyPair? = null
    }
}