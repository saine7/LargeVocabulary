package com.gayedesign.alagiesaine.largevocabulary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    Cursor cursor;
    SearchView searchWord;
    TextView wordtextview, meaningtextview;
    ListView listView;
    ImageButton imageButton;
    ArrayAdapter<String> arrayAdapter;
    boolean listViewVisible = false;
    boolean meaningVisible = false;
    CharSequence meaningText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchWord = (SearchView) findViewById(R.id.searchWord);
        wordtextview = (TextView) findViewById(R.id.wordtextView);
        meaningtextview = (TextView) findViewById(R.id.meaningtextView);
        listView = (ListView) findViewById(R.id.listView);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        listViewVisible = listView.getVisibility() == View.VISIBLE;
        meaningVisible = meaningtextview.getVisibility() == View.VISIBLE;
        meaningText = meaningtextview.getText();

        createDatabase();
        allWords();

        searchWord.setQueryHint("Search word");
        searchWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (meaningText.length() == 0) {
                    listView.setVisibility(View.GONE);
                    searchWord(query);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                listView.setVisibility(View.VISIBLE);
                arrayAdapter.getFilter().filter(newText);
                meaningtextview.setText("");
                wordtextview.setText("");

                return false;
            }
        });
        /*
        searchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordtextview.setVisibility(View.GONE);
                meaningtextview.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });
        */
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meaningtextview.setText("");
                wordtextview.setText("");
                listView.setVisibility(View.VISIBLE);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemClick = parent.getItemAtPosition(position);
                searchWord(itemClick.toString());
                listView.setVisibility(View.GONE);
            }
        });
    }

    public void createDatabase() {
        database = this.openOrCreateDatabase("Vocabulary", Context.MODE_PRIVATE, null);
        File databaseFile = getApplicationContext().getDatabasePath("Vocabulary.db");
        try {
            if (!databaseFile.exists()) {
                database.execSQL("CREATE TABLE IF NOT EXISTS words(word VARCHAR,meaning VARCHAR);");
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void searchWord(CharSequence search) {
        cursor = database.rawQuery("SELECT * FROM words WHERE word = '" + search.toString().trim() + "' ", null);

        int wordIndex = cursor.getColumnIndexOrThrow("word");
        int meaningIndex = cursor.getColumnIndexOrThrow("meaning");
        String word = "";
        String meaning = "";

        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                word = cursor.getString(wordIndex);
                meaning = cursor.getString(meaningIndex);
            } while (cursor.moveToNext());
            wordtextview.setText(word);
            meaningtextview.setText(meaning);
        } else {
            wordtextview.setText("");
            meaningtextview.setText("Word not found.");
        }
    }

    public void allWords() {
        cursor = database.rawQuery("SELECT word FROM words", null);
        int wordIndex = cursor.getColumnIndex("word");
        ArrayList listOfWords = new ArrayList();
        String wordString = "";
        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listOfWords);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            do {
                wordString = cursor.getString(wordIndex);
                listOfWords.add(wordString);
            } while (cursor.moveToNext());
            Collections.sort(listOfWords);
        }
        if (meaningText.length() == 0) {
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_word) {
            Intent addWordIntent = new Intent(getApplicationContext(), AddWordActivity.class);
            startActivity(addWordIntent);
            return true;
        } else if (id == R.id.action_delete_word) {
            Intent deleteWordIntent = new Intent(getApplicationContext(), DeleteWordActivity.class);
            startActivity(deleteWordIntent);
            return true;
        }else if (id == R.id.action_update_word){
            Intent updateWordIntent = new Intent(getApplicationContext(), UpdateActivity.class);
            startActivity(updateWordIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
