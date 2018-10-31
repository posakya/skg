package com.creatu.sapnakoghar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.model_class.order_model_class.Orders;
import com.creatu.sapnakoghar.order_detail.OrderDetailActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context context;
    private List<Orders> ordersList;

    public OrderAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {

        final Orders orders = ordersList.get(position);

        holder.txt_place_by.setText("Place By :" + Html.fromHtml(orders.getUpdated_info().getName()));
        holder.txt_site_code.setText("Site Id :" + Html.fromHtml(orders.getSite_info().getCode()));
        holder.txt_order_name.setText("Ordered Name :" + Html.fromHtml(orders.getOrder_status_name()));
        holder.txt_order_code.setText("Order Code :" + Html.fromHtml(orders.getOrder_code()));
        holder.txt_created_date.setText(Html.fromHtml(orders.getCreated_at()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                try {
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("order_code", orders.getOrder_code());
                    intent.putExtra("order_id", orders.getId());


                    context.startActivity(intent);
                } catch (AndroidRuntimeException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_order_name,txt_site_code,txt_order_code,txt_created_date,txt_place_by;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_created_date = itemView.findViewById(R.id.txt_created_date);
            txt_order_code  = itemView.findViewById(R.id.txt_order_code);
            txt_order_name = itemView.findViewById(R.id.txt_order_name);
            txt_site_code = itemView.findViewById(R.id.txt_site_code);
            txt_place_by = itemView.findViewById(R.id.txt_place_by_name);
        }
    }
}
