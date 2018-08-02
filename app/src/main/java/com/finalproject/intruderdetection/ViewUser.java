package com.finalproject.intruderdetection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.finalproject.intruderdetection.adapters.EventAdapter;
import com.finalproject.intruderdetection.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewUser extends AppCompatActivity {

    @BindView(R.id.img_user_icon)
    CircleImageView imgUserPic;

    @BindView(R.id.txt_phone_number)
    TextView txtPhoneNumber;

    @BindView(R.id.list_user_history)
    RecyclerView listHistory;

    boolean blacklistedStatus;
    private ProgressDialog progressDialog;
    private List<Event> events;
    private String userId;

    @BindView(R.id.txt_username)
    TextView txtUsername;

    @BindView(R.id.txt_email_address)
    TextView txtEmailAddress;
    @BindView(R.id.txt_address)
    TextView txtAddress;

    @BindView(R.id.btn_blacklist)
    Button btnBlacklist;

    @OnClick(R.id.btn_blacklist)
    void onClick() {
        progressDialog.setMessage("Blacklisting user ....");
        progressDialog.show();
        blacklistUser(userId);
    }

    @OnClick(R.id.btn_delete) void onDelete(){
        progressDialog.setMessage("Deleting user ....");
        progressDialog.show();
        deleteUser(userId);
    }

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        ButterKnife.bind(this);

        events = new ArrayList<>();
        setupProgressDialog();
        String username = getIntent().getStringExtra("username");
        String photoURL = getIntent().getStringExtra("avatar_id");
        String phone = getIntent().getStringExtra("phone");
        String email = getIntent().getStringExtra("email");
        String address = getIntent().getStringExtra("address");
        boolean blacklisted = getIntent().getBooleanExtra("blacklisted", false);
        id = getIntent().getStringExtra("id");
        userId = id;
        blacklistedStatus = blacklisted;
        if (blacklisted) {
            btnBlacklist.setText("UNBLACKLIST");
        }
        txtPhoneNumber.setText("Contact no: " + phone);
        txtUsername.setText("Name: " + username);
        txtEmailAddress.setText("Email: " + email);
        txtAddress.setText("Address: " + address);
        Glide.with(this).load(Api.FILE_SERVER_GET_URL + photoURL).into(imgUserPic);

        getUserHistory(userId);
    }

    public void deleteUser(String id){
        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
        Call<ResponseBody> delete  = api.delete(Api.BASE_URL+"/user/delete/"+id);
        delete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.cancel();
                if(response.isSuccessful()){
                    try {
                        Log.d("bs", response.body().string());
                        Toast.makeText(ViewUser.this, "User is deleted!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ViewUser.this, Home.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ViewUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void blacklistUser(String id) {
        if (blacklistedStatus){
            Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
            retrofit2.Call<ResponseBody> loginCall = api.blacklist(Api.BASE_URL + "user/blacklist/" + id, !blacklistedStatus);
            loginCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.cancel();
                    if (response.isSuccessful()) {
                        try {
                            Log.d("bs", response.body().string());
                            btnBlacklist.setText("BLACKLIST");
                            blacklistedStatus = true;
                            startActivity(new Intent(getApplicationContext(), Home.class));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else {


            Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
            retrofit2.Call<ResponseBody> loginCall = api.blacklist(Api.BASE_URL + "user/blacklist/" + id, !blacklistedStatus);
            loginCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            Log.d("bs", response.body().string());
                            btnBlacklist.setText("UNBLACKLIST");
                            blacklistedStatus = true;
                            startActivity(new Intent(getApplicationContext(), Home.class));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void getUserHistory(String owner) {
        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
        Call<ResponseBody> call = api.getUser(Api.BASE_URL + "event/" + owner);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String res = response.body().string();
                        //Log.d("data", res);
                        JSONArray eventArray = new JSONArray(res);
                        for (int i = 0; i < eventArray.length(); i++) {
                            Log.d("data", eventArray.get(i).toString());
                            JSONObject object = eventArray.getJSONObject(i);
                            String owner = object.getString("owner");
                            String event_id = object.getString("event_id");
                            String status = object.getString("status");
                            String created = object.getString("created");

                            Event event = new Event(event_id, owner, created, status);
                            getUser(owner, event);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getUser(String owner, final Event event) {
        Api.OpenfaceApi openfaceApi = Api.getClient().create(Api.OpenfaceApi.class);
        Call<ResponseBody> call = openfaceApi.getUser(Api.BASE_URL + "user/" + owner);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
//                        Log.d("Name", response.body().string());
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject object = jsonObject.getJSONObject("data");
                        String name = object.getString("username");
                        JSONArray avatarArray = object.getJSONArray("avatars");
                        String avatar_url = avatarArray.getJSONObject(0).getString("avatar_url");
                        String avatar_id = avatarArray.getJSONObject(0).getString("avatar_id");

                        event.setAvatar(avatar_id);
                        event.setUsername(name);
                        events.add(event);
                        EventAdapter adapter = new EventAdapter(events, ViewUser.this);
                        listHistory.setAdapter(adapter);
                        listHistory.setLayoutManager(new LinearLayoutManager(ViewUser.this));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);

    }
}
