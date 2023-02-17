package com.example.carrenting.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carrenting.FragmentPages.Customer.CustomerNotificationFragment;
import com.example.carrenting.Model.Notification;
import com.example.carrenting.Model.User;
import com.example.carrenting.R;
import com.example.carrenting.Service.Notification.NotificationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    CustomerNotificationFragment customerNotificationFragment;
    Notification noti;
    ArrayList<Notification> mNoti;

    FirebaseFirestore dtb;
    String Name, ProvideID;


    public NotificationAdapter(CustomerNotificationFragment mContext, ArrayList<Notification>mNoti){
        this.customerNotificationFragment=mContext;
        this.mNoti=mNoti;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(customerNotificationFragment.getActivity()).inflate(R.layout.item_notification_customer, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        noti = mNoti.get(position);

        dtb = FirebaseFirestore.getInstance();
        ProvideID=noti.getProvider_id();
        getuser(ProvideID);
        holder.name.setText(Name);
        holder.id.setText(noti.getNoti_id());


        if(noti.getStatus().equals( "Dang cho"))
        {
            holder.status.setText("Đang chờ");
        }
        else
        {
            if(noti.getStatus().equals( "Xac nhan"))
            {
                holder.status.setText("Nhà cung cấp đã xác nhận");
            }
            else
            {
                holder.status.setText("Nhà cung cấp không xác nhận");
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(customerNotificationFragment.getActivity(), NotificationActivity.class);
                intent.putExtra("NotiID", noti.getNoti_id());
                customerNotificationFragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoti.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, status,id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_noti_name);
            status=itemView.findViewById(R.id.tv_Status);
            id=itemView.findViewById(R.id.tv_noti_ID);


        }
    }

    private void getuser(String ProvideID){
        dtb.collection("Users")
                .whereEqualTo("user_id", ProvideID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                User user = new User();
                                user.setUser_id(document.get("user_id").toString());
                                user.setUsername(document.get("username").toString());
                                user.setEmail(document.get("email").toString());
                                user.setPhoneNumber(document.get("phoneNumber").toString());
                                Name=user.getUsername();
                            }
                        } else {

                        }
                    }
                });
    }




}
