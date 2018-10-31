package com.creatu.sapnakoghar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.model_class.ParticularResults;
import com.creatu.sapnakoghar.order_section.AddOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderListItemAdapter extends RecyclerView.Adapter<OrderListItemAdapter.MyViewHolder> {

    private Context context;
    private List<ParticularResults> particularResults;
    private ArrayList<Integer> itemId = new ArrayList<>();
    private ArrayList<Integer> quantity = new ArrayList<>();

    AddOrderActivity addOrderActivity = new AddOrderActivity();


    public OrderListItemAdapter(Context context, List<ParticularResults> particularResults) {
        this.context = context;
        this.particularResults = particularResults;
    }

    @NonNull
    @Override
    public OrderListItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderListItemAdapter.MyViewHolder holder, int position) {

        final ParticularResults results = particularResults.get(position);




        holder.item_checkBox.setText(Html.fromHtml(results.getName()));
        holder.edit_quantity.getText().toString();
        holder.edit_quantity.setText("0");

        holder.item_checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (itemId.contains(results.getId())){
                        itemId.remove(results.getId());
                    }else{


                        itemId.add(Integer.parseInt(results.getId()));
                        quantity.add(Integer.parseInt(holder.edit_quantity.getText().toString().trim()));
                        addOrderActivity.setItem(itemId,quantity);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return particularResults.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox item_checkBox;
        EditText edit_quantity;

        public MyViewHolder(View itemView) {
            super(itemView);

            item_checkBox = itemView.findViewById(R.id.item_check_box);
            edit_quantity = itemView.findViewById(R.id.edit_quantity);

        }
    }
}


