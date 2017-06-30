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

public class UpdateActivity extends AppCompatActivity{
    SQLiteDatabase database;
    Cursor cursor;
    EditText meaning_update_field,update_word_field;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_word);

        meaning_update_field = (EditText) findViewById(R.id.meaning_update_field);
        update_word_field = (EditText) findViewById(R.id.update_word_field);

        createDatabase();
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

    public void fetch_meaning(View view) {
        String word = update_word_field.getText().toString().toLowerCase().trim();
        //String meaning = meaning_update_field.getText().toString().toLowerCase().trim();

        cursor = database.rawQuery("SELECT word,meaning FROM words WHERE word = '" + word + "' ", null);
        int meaningIndex = cursor.getColumnIndex("meaning");

        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() > 0) {
            String meaningInfo = "";
            do {
                meaningInfo = cursor.getString(meaningIndex);
            } while (cursor.moveToNext());
            meaning_update_field.setText(meaningInfo);
        } else {
            Toast.makeText(getApplicationContext(),"Word NOT in the list. Thus meaning cannot be fetched",Toast.LENGTH_LONG).show();
        }

    }


    public void update(View view) {
        String word = update_word_field.getText().toString().toLowerCase().trim();
        String meaning = meaning_update_field.getText().toString().toLowerCase().trim();

        if (word.length() < 2){
            Toast.makeText(getApplicationContext(),"Word must be greater than one character.",Toast.LENGTH_LONG).show();

        }else if (meaning.length() < 3){
            Toast.makeText(getApplicationContext(),"Meaning must be greater than two characters.",Toast.LENGTH_LONG).show();
        }else {
            database.execSQL("UPDATE words SET meaning = '"+meaning+"' WHERE word = '"+word+"' ");
            Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_SHORT).show();

            update_word_field.setText("");
            meaning_update_field.setText("");
        }



    }
}
