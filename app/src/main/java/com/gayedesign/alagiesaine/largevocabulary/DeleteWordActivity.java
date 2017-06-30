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

public class DeleteWordActivity extends AppCompatActivity{
    SQLiteDatabase database;
    Cursor cursor;
    EditText delete_word_field;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_word);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        delete_word_field = (EditText) findViewById(R.id.delete_word_field);
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

    public void deleteWord(View view) {
        String word = delete_word_field.getText().toString().toLowerCase().trim();

        if (word.length() < 2){
            Toast.makeText(getApplicationContext(),"Word length must be greater than 1",Toast.LENGTH_LONG).show();
        }else {
            try {

                cursor = database.rawQuery("SELECT word FROM words WHERE word = '"+word+"' ",null);
                cursor.moveToFirst();
                do {
                    if (cursor != null && cursor.getCount() > 0){
                        database.execSQL("DELETE FROM words WHERE word = '"+word+"' ");
                        Toast.makeText(getApplicationContext(),"Word deleted successfully.",Toast.LENGTH_SHORT).show();
                        delete_word_field.setText("");
                    }else if (cursor == null || cursor.getCount() < 1){
                        Toast.makeText(getApplicationContext(),"Word NOT in list. Thus cannot be deleted.",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Word NOT deleted. Unknown error.",Toast.LENGTH_LONG).show();
                    }

                }while (cursor.moveToNext());
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),"Word NOT deleted. Please try again later.",Toast.LENGTH_LONG).show();
            }
        }
    }
}
