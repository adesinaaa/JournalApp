package com.divineventures.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity {

    private EditText entry_text;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        entry_text = findViewById(R.id.entry_text);
        final String text = entry_text.getText().toString();
        save = findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Entry",text);
                startActivity(intent);

            }
        });


    }

}
