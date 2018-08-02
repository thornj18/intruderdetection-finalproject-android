package com.finalproject.intruderdetection.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.finalproject.intruderdetection.Api;
import com.finalproject.intruderdetection.OnItemClickListener;
import com.finalproject.intruderdetection.R;
import com.finalproject.intruderdetection.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by TKPC on 1/16/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<User> users;
    private Context context;
    private OnItemClickListener clickListener;


    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context, users);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtUserName.setText("Name: " + user.getUsername());
        holder.txtPhoneNumber.setText("Contact no: " + user.getPhoneNumber());
        holder.txtEmail.setText("Email: " + user.getEmail());
        holder.txtAddress.setText("Address: " + user.getAddress());
        if (users.get(position).getBlacklisted()) {
            holder.cardView.setBackgroundColor(Color.RED);
        }
        Glide.with(context).load(Api.FILE_SERVER_GET_URL + user.getAvatarId()).into(holder.imgUserPhoto);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override

    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtUserName;
        private TextView txtPhoneNumber;
        private TextView txtEmail;
        private TextView txtAddress;
        private CircleImageView imgUserPhoto;
        private CardView cardView;
        private Context context;
        private List<User> users;

        public ViewHolder(View itemView, Context context, List<User> users) {
            super(itemView);
            this.context = context;
            this.users = users;

            cardView = itemView.findViewById(R.id.cardLayout);
            txtPhoneNumber = itemView.findViewById(R.id.txt_phone_number);
            txtUserName = itemView.findViewById(R.id.txt_username);
            txtEmail = itemView.findViewById(R.id.txt_email_address);
            txtAddress = itemView.findViewById(R.id.txt_address);
            imgUserPhoto = itemView.findViewById(R.id.img_user_icon);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onClick(view, position);
        }
    }
}
