package com.finalproject.intruderdetection.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.intruderdetection.Api;
import com.finalproject.intruderdetection.OnItemClickListener;
import com.finalproject.intruderdetection.R;
import com.finalproject.intruderdetection.models.Event;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TKPC on 7/12/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Event> events;
    private Context context;
    private OnItemClickListener clickListener;

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;

    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_user_visit_item, parent, false);
        EventAdapter.ViewHolder viewHolder = new EventAdapter.ViewHolder(view, context, events);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.txtUserName.setText(event.getUsername());
        holder.txtStatus.setText(event.getStatus());
        Glide.with(context).load(Api.FILE_SERVER_GET_URL+event.getAvatar()).into(holder.imgUserPhoto);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            Date d = sdf.parse(event.getCreated());
            long milliseconds = d.getTime();
            holder.txtTimeAgo.setText(TimeAgo.using(milliseconds));
            holder.txtDate.setText(d.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtUserName;
        private TextView txtStatus;
        private TextView txtTimeAgo;
        private TextView txtDate;
        private CircleImageView imgUserPhoto;
        private Context context;
        private List<Event> events;

        public ViewHolder(View itemView, Context context, List<Event> events) {
            super(itemView);
            this.context = context;
            this.events = events;


            txtUserName = itemView.findViewById(R.id.txt_username);
            imgUserPhoto = itemView.findViewById(R.id.img_user_icon);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtDate = itemView.findViewById(R.id.txt_date_entrance);
            txtTimeAgo = itemView.findViewById(R.id.txt_time_entrance);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onClick(view, position);
        }
    }
}
