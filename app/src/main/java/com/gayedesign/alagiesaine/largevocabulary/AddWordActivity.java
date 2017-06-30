package com.gayedesign.alagiesaine.largevocabulary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class AddWordActivity extends AppCompatActivity {
    SQLiteDatabase database;
    EditText add_word_field,word_meaning_field;
    Cursor cursor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_word);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_word_field = (EditText) findViewById(R.id.add_word_field);
        word_meaning_field = (EditText) findViewById(R.id.word_meaning_field);
        createDatabase();
    }
    public void createDatabase(){
        database = this.openOrCreateDatabase("Vocabulary", Context.MODE_PRIVATE,null);
        File databaseFile = getApplicationContext().getDatabasePath("Vocabulary.db");
        try{
            if (!databaseFile.exists()){
                database.execSQL("CREATE TABLE IF NOT EXISTS words(word VARCHAR,meaning VARCHAR);");
                //Toast.makeText(getApplicationContext(),"Database Created",Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void addWord(View view) {
        String word = add_word_field.getText().toString().toLowerCase().trim();
        String meaning = word_meaning_field.getText().toString().toLowerCase().trim();

        cursor = database.rawQuery("SELECT word FROM words WHERE word = '"+word+"' ",null);
        cursor.moveToFirst();

        do {
            if (cursor != null && cursor.getCount() > 0){
                Toast.makeText(getApplicationContext(),"Word NOT added.Its already in the list.",Toast.LENGTH_LONG).show();
                word_meaning_field.setText("");
            }else {
                if (word.length() < 2){
                    Toast.makeText(getApplicationContext(),"Word must be greater than one character.",Toast.LENGTH_LONG).show();

                }else if (meaning.length() < 3){
                    Toast.makeText(getApplicationContext(),"Meaning must be greater than two characters.",Toast.LENGTH_LONG).show();
                }else {
                    try {
                        database.execSQL("INSERT INTO words(word,meaning) VALUES('"+word+"','"+meaning+"');");
                        Toast.makeText(getApplicationContext(),"Word Added successfully.",Toast.LENGTH_SHORT).show();
                        add_word_field.setText("");
                        word_meaning_field.setText("");
                    }catch (Exception ex){
                        Toast.makeText(getApplicationContext(),"Word NOT added.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }while (cursor.moveToNext());


    }
}
