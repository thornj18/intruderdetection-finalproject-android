package com.finalproject.intruderdetection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @BindView(R.id.edt_username)
    EditText edtUsername;

    @BindView(R.id.edt_password)
    EditText edtPassword;
    private ProgressDialog progressDialog;

    @OnClick(R.id.btn_login) void onLogin(){
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        if (validate()){
            if (edtUsername.getText().toString().equalsIgnoreCase("admin") && edtPassword.getText().toString().equalsIgnoreCase("admin123")){
                progressDialog.cancel();
                startActivity(new Intent(getApplicationContext(), Home.class));
                dontGoBack();
            }else{
                Toast.makeText(getApplicationContext(), "Authentication error, password is 'admin'", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(Login.this);
        setupProgressDialog();

    }
    public boolean validate() {
        String phone = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void help(){
        startActivity(new Intent(getApplicationContext(), Home.class));
        dontGoBack();
    }


    private void dontGoBack() {
        this.finish();
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Logging in ...");
    }
}
