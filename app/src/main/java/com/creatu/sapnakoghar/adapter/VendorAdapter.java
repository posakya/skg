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
import com.creatu.sapnakoghar.model_class.order_detail_model_class.Order_venders;

import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.MyViewHolder> {

    Context context;
    List<Order_venders> order_vendersList;

    public VendorAdapter(Context context, List<Order_venders> order_vendersList) {
        this.context = context;
        this.order_vendersList = order_vendersList;
    }

    @NonNull
    @Override
    public VendorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_detail,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAdapter.MyViewHolder holder, int position) {
        Order_venders order_venders = order_vendersList.get(position);

        holder.txt_email.setText(Html.fromHtml(order_venders.getEmail()));
        holder.txt_name.setText(Html.fromHtml(order_venders.getName())+" |");

    }

    @Override
    public int getItemCount() {
        return order_vendersList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_name,txt_email;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_vendor_name);
            txt_email = itemView.findViewById(R.id.txt_vendor_email);
        }
    }
}
