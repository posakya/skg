package com.creatu.sapnakoghar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.model_class.notification_model_class.Notifications;
import com.creatu.sapnakoghar.order_detail.OrderDetailActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    private List<Notifications> notificationsList;

    public NotificationAdapter(Context context, List<Notifications> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {

        final Notifications notifications = notificationsList.get(position);

        holder.txt_message.setText(Html.fromHtml(notifications.getNotification()));

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = 0;
        try {
            time = sdf.parse(notifications.getCreated()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();

        final CharSequence ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        holder.txt_created_date.setText(String .valueOf(ago));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                if (notifications.getContent().getType().equals("order")){

                    try {
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        intent.putExtra("order_code", orders.getOrder_code());
                        intent.putExtra("order_id", notifications.getContent().getId());


                        context.startActivity(intent);
                    } catch (AndroidRuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        });







    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_message,txt_created_date;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_created_date = itemView.findViewById(R.id.txt_created_date);
            txt_message  = itemView.findViewById(R.id.txt_message);

        }
    }
}
