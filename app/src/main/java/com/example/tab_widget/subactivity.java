package com.example.tab_widget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class subactivity extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subactivity);
        Log.d("created","yes");
        this.getEditTextObject();
    }

    public void getEditTextObject(){
        editText_name = (EditText)findViewById(R.id.edittext_name);
        editText_phone = (EditText)findViewById(R.id.edittext_phone);
    }

    public void backBtnClicked(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        Log.d("백","ㄴㅇㄹ");
    }

    public void contactadd(View view)
    {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("name", editText_name.getText().toString());
        resultIntent.putExtra("phone", editText_phone.getText().toString());
        Log.d("sub에서",editText_name.getText().toString());
        setResult(Code.resultCode, resultIntent);
        finish();
    }


}