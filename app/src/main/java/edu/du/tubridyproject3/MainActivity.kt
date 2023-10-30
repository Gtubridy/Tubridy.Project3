package edu.du.tubridyproject3
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    private lateinit var wordInput: EditText
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordInput = findViewById(R.id.wordEntryView)
        searchButton = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val word = wordInput.text.toString()
            DictionaryAsyncTask().execute(word)
        }
    }

    private inner class DictionaryAsyncTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {
            var result = ""

            try {
                val url = URL("https://api.dictionaryapi.dev/api/v2/entries/en/${params[0]}")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == HttpsURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        result += line
                    }

                    inputStream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (!TextUtils.isEmpty(result)) {
                try {
                    Log.i(TAG, "onPostExecute: "+(result))
                    val jsonArray = JSONArray(result)
                    val jsonObject = jsonArray.getJSONObject(0)
                    val intent = Intent(this@MainActivity, ListActivity::class.java)
                    intent.putExtra("word", jsonObject.getString("word"))
                    intent.putExtra("meanings", jsonObject.getJSONArray("meanings").toString())
                    startActivity(intent)

                } catch (e: JSONException) {

                }
            }
        }
    }
}
