package com.finalproject.intruderdetection.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finalproject.intruderdetection.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Live extends Fragment {


    public Live() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_live, container, false);

        try {

            JSONObject object = new JSONObject();
            JSONArray array = object.getJSONArray("luggage_instances");

            String id = array.getJSONObject(0).getString("id");
            String ticketId = array.getJSONObject(0).getString("ticket_id");
            String buggageCode = array.getJSONObject(0).getString("buggage_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

}
