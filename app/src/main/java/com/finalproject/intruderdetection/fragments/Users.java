package com.finalproject.intruderdetection.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.finalproject.intruderdetection.Api;
import com.finalproject.intruderdetection.Home;
import com.finalproject.intruderdetection.OnItemClickListener;
import com.finalproject.intruderdetection.R;
import com.finalproject.intruderdetection.ViewUser;
import com.finalproject.intruderdetection.adapters.UserAdapter;
import com.finalproject.intruderdetection.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Users extends Fragment {
    @BindView(R.id.list_users)
    RecyclerView listUsers;



    private ProgressDialog progressDialog;
    private List<User> users;




    public Users() {
        // Required empty public constructor
    }

    public static Users newInstance(Home home){
        return new Users();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);

        users = new ArrayList<>();
        setupProgressDialog();
        progressDialog.show();
        getUsers();

        return view;
    }

    public void getUsers() {
        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
        retrofit2.Call<ResponseBody> loginCall = api.get();
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        progressDialog.cancel();
                        JSONArray array = new JSONArray(response.body().string());
                        for (int i = 0; i < array.length(); i++) {
                            insertData(array.getJSONObject(i));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                progressDialog.cancel();

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void insertData(JSONObject obj) throws JSONException {
        String name = obj.getString("username");
        String phoneNumber = obj.getString("phone");
        JSONArray avatarArray = obj.getJSONArray("avatars");
        String avatar_url = avatarArray.getJSONObject(0).getString("avatar_url");
        String avatar_id = avatarArray.getJSONObject(0).getString("avatar_id");
        String email = obj.getString("email");
        String address = obj.getString("address");
        String id = obj.getString("user_id");
        Boolean blacklisted = obj.getBoolean("blacklisted");

        User user = new User(name, phoneNumber, avatar_url, id, blacklisted, email, address, avatar_id);
        users.add(user);
        UserAdapter adapter = new UserAdapter(users, getActivity());
        listUsers.setAdapter(adapter);
        listUsers.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ViewUser.class);
                intent.putExtra("username", users.get(position).getUsername());
                intent.putExtra("phone", users.get(position).getPhoneNumber());
                intent.putExtra("avatar_id", users.get(position).getAvatarId());
                intent.putExtra("id", users.get(position).getId());
                intent.putExtra("blacklisted", users.get(position).getBlacklisted());
                intent.putExtra("email", users.get(position).getEmail());
                intent.putExtra("address", users.get(position).getAddress());

                startActivity(intent);
            }
        });
    }


    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Getting users...");
    }


}
