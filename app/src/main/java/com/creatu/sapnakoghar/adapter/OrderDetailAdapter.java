package com.creatu.sapnakoghar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.Order_particulars;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    Context context;
    List<Order_particulars> order_particularsList;

    public OrderDetailAdapter(Context context, List<Order_particulars> order_particularsList) {
        this.context = context;
        this.order_particularsList = order_particularsList;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.MyViewHolder holder, int position) {

        Order_particulars order_particulars = order_particularsList.get(position);
        holder.txt_order_qty.setText("Qty : "+Html.fromHtml(order_particulars.getRequested_quantity())+" |");
        holder.txt_order_price.setText("Rs. "+Html.fromHtml(order_particulars.getPrice()));
        holder.txt_order_name.setText(Html.fromHtml(order_particulars.getName())+" |");

    }

    @Override
    public int getItemCount() {
        return order_particularsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_order_name,txt_order_price,txt_order_qty;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_order_name = itemView.findViewById(R.id.txt_order_name);
            txt_order_price = itemView.findViewById(R.id.txt_order_price);
            txt_order_qty = itemView.findViewById(R.id.txt_order_qty);
        }
    }
}
