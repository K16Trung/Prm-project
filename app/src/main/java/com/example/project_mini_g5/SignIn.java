package com.example.project_mini_g5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity implements View.OnClickListener{


    private EditText username, password;
    private Button signin;


    private final String REQUIRE = "Require";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signIn);

        signin.setOnClickListener(this);
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(username.getText().toString())) {
            username.setError(REQUIRE);
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError(REQUIRE);
            return false;
        }
        return true;
    }

    private void signIn() {
        String name = "sa";
        String pass = "12345";
        if (!checkInput()) {
            return;
        }
        if (username.getText().toString().equals(name) && password.getText().toString().equals(pass)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            username.setError(REQUIRE);
            password.setError(REQUIRE);
            Toast.makeText(SignIn.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signIn) {
            signIn();
        }
    }
}
