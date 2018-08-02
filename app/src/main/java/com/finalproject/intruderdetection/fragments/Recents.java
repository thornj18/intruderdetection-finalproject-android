package com.finalproject.intruderdetection.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.intruderdetection.Api;
import com.finalproject.intruderdetection.Home;
import com.finalproject.intruderdetection.R;
import com.finalproject.intruderdetection.adapters.EventAdapter;
import com.finalproject.intruderdetection.adapters.UserAdapter;
import com.finalproject.intruderdetection.models.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Recents extends Fragment {

    @BindView(R.id.list_events)
    RecyclerView listEvents;

    List<Event> events;

    public Recents() {
        // Required empty public constructor
    }

    public static Recents newInstance(Home home){
        return new Recents();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recemts, container, false);
        ButterKnife.bind(this, view);

        events = new ArrayList<>();

        getRecentVisitors();
        return view;
    }


    public void getRecentVisitors() {
        Api.OpenfaceApi api = Api.getClient().create(Api.OpenfaceApi.class);
        Call<ResponseBody> call = api.getEvents();
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

                            Event event = new Event(event_id, owner, created,status);
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
        Call<ResponseBody> call = openfaceApi.getUser(Api.BASE_URL+"user/"+owner);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
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
                        EventAdapter adapter = new EventAdapter(events, getActivity());
                        listEvents.setAdapter(adapter);
                        listEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
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

}
