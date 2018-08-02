package com.finalproject.intruderdetection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.finalproject.intruderdetection.models.RegisterModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser extends AppCompatActivity {
    ProgressDialog progressDialog;


    @BindView(R.id.edt_first_name)
    EditText edtFirstName;

    @BindView(R.id.edt_last_name)
    EditText edtLastName;

    @BindView(R.id.edt_phone_number)
    EditText edtPhoneNumber;

    @BindView(R.id.edt_email_address)
    EditText edtEmail;
    @BindView(R.id.edt_address)
    EditText edtAddress;

    @BindView(R.id.btn_save)
    Button btnSave;

    @OnClick(R.id.btn_save)
    void onClick() {
        validate(edtFirstName, edtLastName, edtPhoneNumber, edtEmail, edtAddress);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        setupProgressDialog();

    }

    private void registerUser(String firstName, String lastName, String phoneNumber, String email, String address) {
        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);

        retrofit2.Call<ResponseBody> regsiter = api.register(new RegisterModel(firstName + lastName, email, address, phoneNumber));
        regsiter.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
                        JSONObject object = new JSONObject(response.body().string());
                        String username = object.getString("username");
                        String userId = object.getString("user_id");

                        startActivity(new Intent(getApplicationContext(), AddImages.class).putExtra("username", username).putExtra("user_id", userId));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                progressDialog.cancel();

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void workOnData(String response) {

        try {
            JSONObject object = new JSONObject(response);
            String username = object.getString("username");
            String userId = object.getString("user_id");

            startActivity(new Intent(this, AddImages.class).putExtra("username", username).putExtra("user_id", userId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void validate(EditText edtFirstName, EditText edtLastName, EditText edtPhoneNumber, EditText edtEmail, EditText edtAddress) {
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String email = edtEmail.getText().toString();
        String address = edtAddress.getText().toString();

        if (firstName.isEmpty() && lastName.isEmpty() && phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_LONG).show();
        } else {
            registerUser(firstName, lastName, phoneNumber, email, address);
        }
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Registering in ...");
    }
}
