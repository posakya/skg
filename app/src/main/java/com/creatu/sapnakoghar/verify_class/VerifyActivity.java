package com.creatu.sapnakoghar.verify_class;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.creatu.sapnakoghar.MainActivity;
import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.bill_section.AddBillActivity;
import com.creatu.sapnakoghar.model_class.ParticularModelClass;
import com.creatu.sapnakoghar.model_class.ParticularResults;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.OrderDetailModelClass;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.Order_particulars;
import com.creatu.sapnakoghar.order_section.AddOrderActivity;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;
import com.creatu.sapnakoghar.vendor_section.AddVendorActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    JsonObject jsonObject;
    JsonArray particularsArray;
    ShowProgress progress;
    RecyclerView recyclerView;
    Toolbar toolbar;
    String siteID,userId;
    String orderCode,orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        toolbar = findViewById(R.id.tool_bar);
        recyclerView = findViewById(R.id.item_recycler_view);
        progress = new ShowProgress(VerifyActivity.this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Verify Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jsonObject = new JsonObject();
        particularsArray = new JsonArray();

        try {
            siteID = getIntent().getExtras().getString("order_id");
            orderCode = getIntent().getExtras().getString("order_code");
            System.out.println("SiteID : "+siteID);
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            userId = sharedPreferences.getString("id",null);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (siteID != null){
            getSupportActionBar().setTitle(orderCode);
            getParticular(siteID);

        }

    }


    public void getParticular(String orderId){

        ApiInterface orderDetailInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<OrderDetailModelClass> orderDetailModelClassCall = orderDetailInterface.getOrderDetails(orderId);

        progress.showProgress();

        orderDetailModelClassCall.enqueue(new Callback<OrderDetailModelClass>() {
            @Override
            public void onResponse(Call<OrderDetailModelClass> call, Response<OrderDetailModelClass> response) {


                if (response.body().getResults().getOrder_particulars().size() != 0){
                    VerifyOrder orderDetailAdapter = new VerifyOrder(getApplicationContext(),response.body().getResults().getOrder_particulars());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(orderDetailAdapter);
                    progress.hideProgress();
                }else{
                    Toast.makeText(VerifyActivity.this, "No Order data found", Toast.LENGTH_SHORT).show();
                    progress.hideProgress();
                }


            }

            @Override
            public void onFailure(Call<OrderDetailModelClass> call, Throwable t) {
                Toast.makeText(VerifyActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }




    public void postOrder(String user_id){

        progress.showProgress();

        ApiInterface addOrderInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        if (siteID != null){
            jsonObject.addProperty("site_order_id",Integer.parseInt(siteID));
        }




        jsonObject.add("particulars",particularsArray);

        System.out.println("Particulars : "+jsonObject);


        Call<JsonObject> call = addOrderInterface.verifyOrder(jsonObject,user_id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonObject2 = new JSONObject(response.body().toString());

                        int status = jsonObject2.optInt("status");
                        String results = jsonObject2.optString("results");
                        String message = jsonObject2.optString("message");

                        if (status == 200){
                            progress.hideProgress();
                            Toast.makeText(VerifyActivity.this, results, Toast.LENGTH_SHORT).show();

                            try{

                                SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                                String  user_type = sharedPreferences.getString("user_type",null);
                                if (user_type.equals("site_engineer")){
                                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), AddBillActivity.class);
                                    intent.putExtra("order_code",orderCode);
                                    intent.putExtra("order_id",siteID);
                                    startActivity(intent);
                                }


                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }


                        }else if (status == 400){
                            progress.hideProgress();
                            Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            progress.hideProgress();
                            Toast.makeText(VerifyActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.hideProgress();
                    Toast.makeText(VerifyActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.hideProgress();
            }
        });

    }

    public void setItem(ArrayList<Integer> itemId, final ArrayList<Integer> quantity){

        JsonObject jsonObject1 = new JsonObject();

        for (int i=0;i<itemId.size();i++){
            jsonObject1.addProperty("id",itemId.get(i));
            jsonObject1.addProperty("quantity",quantity.get(i));
        }

        particularsArray.add(jsonObject1);


    }

    public void submit(View view) {
        postOrder(userId);
    }

    public class VerifyOrder extends RecyclerView.Adapter<VerifyOrder.MyViewHolder> {

        private Context context;
        private List<Order_particulars> particularResults;
        private ArrayList<Integer> itemId = new ArrayList<>();
        private ArrayList<Integer> quantity = new ArrayList<>();

        AddOrderActivity addOrderActivity = new AddOrderActivity();


        public VerifyOrder(Context context, List<Order_particulars> particularResults) {
            this.context = context;
            this.particularResults = particularResults;
        }

        @NonNull
        @Override
        public VerifyOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

            final Order_particulars results = particularResults.get(position);

            holder.item_checkBox.setText(Html.fromHtml(results.getName()));
            holder.edit_quantity.setText(Html.fromHtml(results.getRequested_quantity()));
//            holder.edit_quantity.getText().toString();
//            holder.edit_quantity.setText("0");

            holder.item_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.edit_quantity.getText().toString().equals("0")){
                        Toast.makeText(context, "Please insert quantity", Toast.LENGTH_SHORT).show();
                        holder.item_checkBox.setChecked(false);
                    }else{
                        try {
                            if (itemId.contains(results.getId())){
                                itemId.remove(results.getId());
                            }else{


                                itemId.add(Integer.parseInt(results.getId()));
                                quantity.add(Integer.parseInt(holder.edit_quantity.getText().toString().trim()));
                                setItem(itemId,quantity);
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                invalidateOptionsMenu();
                return super.onOptionsItemSelected(item);
        }
    }
}

