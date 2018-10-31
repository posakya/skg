package com.creatu.sapnakoghar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.TouchImageView;
import com.creatu.sapnakoghar.model_class.bill_model_class.Order_bills;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    Context context;
    List<Order_bills> order_bills;

    public BillAdapter(Context context, List<Order_bills> order_bills) {
        this.context = context;
        this.order_bills = order_bills;
    }

    @NonNull
    @Override
    public BillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.MyViewHolder holder, int position) {

        final Order_bills orders = order_bills.get(position);

        holder.txt_place_by.setText("Place By :"+ Html.fromHtml(orders.getUpdated_info().getName()));
        holder.txt_site_code.setText("Site Id :"+Html.fromHtml(orders.getSite_info().getCode()));
        holder.txt_order_name.setText("Ordered Name :"+Html.fromHtml(orders.getBill_status()));
        holder.txt_order_code.setText("Order Code :"+Html.fromHtml(orders.getOrder_code()));
        holder.txt_created_date.setText(Html.fromHtml(orders.getCreated_at()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context,android.R.style.Theme_Holo_Light);

                //dialog action
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                dialog.setContentView(R.layout.activity_bill_detail);

                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                //style id
                //   dialog.setTitle("Reservation");
                dialog.setCanceledOnTouchOutside(true);

                TouchImageView imageView1 = dialog.findViewById(R.id.imageView);
                ImageView cancel = dialog.findViewById(R.id.cancel);
                final ProgressBar progressBar1 = dialog.findViewById(R.id.progressBar);

                Picasso.get().load(orders.getBill_image()).placeholder(R.drawable.ic_camera_alt_dp).error(R.drawable.ic_camera_alt_dp).into(imageView1, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar1.setVisibility(View.GONE);
                    }


                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return order_bills.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

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
