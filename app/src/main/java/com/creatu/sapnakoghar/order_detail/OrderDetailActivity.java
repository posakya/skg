package com.creatu.sapnakoghar.order_detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.adapter.OrderDetailAdapter;
import com.creatu.sapnakoghar.adapter.VendorAdapter;
import com.creatu.sapnakoghar.bill_section.AddBillActivity;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.OrderDetailModelClass;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;
import com.creatu.sapnakoghar.vendor_section.AddVendorActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    RecyclerView orderRecyclerView,vendorRecyclerView;
    TextView txtSiteId,txt_created_by,txt_created_date,txt_description;
    Button btnVerifyDelivery;
    ShowProgress progress;
    Toolbar toolbar;
    ImageView imgEdit;
    String orderCode,orderId;
    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        toolbar = findViewById(R.id.tool_bar);
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        vendorRecyclerView = findViewById(R.id.vendorRecyclerView);
        txt_created_by = findViewById(R.id.txt_created_by);
        txt_created_date = findViewById(R.id.txt_created_date);
        txt_description = findViewById(R.id.order_description);
        txtSiteId = findViewById(R.id.txt_site_id);
        btnVerifyDelivery = findViewById(R.id.btnVerifyDelivery);
        progress = new ShowProgress(OrderDetailActivity.this);
        imgEdit = findViewById(R.id.imgEdit);


        setSupportActionBar(toolbar);


        orderCode = getIntent().getExtras().getString("order_code");
        orderId = getIntent().getExtras().getString("order_id");

        getSupportActionBar().setTitle(orderCode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddVendorActivity.class);
                intent.putExtra("order_code",orderCode);
                intent.putExtra("order_id",orderId);
                startActivity(intent);
            }
        });

        if (orderId != null){
            getOrderDetails(orderId);
        }

        btnVerifyDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBillActivity.class);
                intent.putExtra("order_code",orderCode);
                intent.putExtra("order_id",orderId);
                startActivity(intent);
            }
        });

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            String  userId = sharedPreferences.getString("id",null);
            user_type = sharedPreferences.getString("user_type",null);

            if (user_type.equals("site")){
                btnVerifyDelivery.setVisibility(View.GONE);
            }


        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void getOrderDetails(String orderId){

        ApiInterface orderDetailInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<OrderDetailModelClass> orderDetailModelClassCall = orderDetailInterface.getOrderDetails(orderId);

        progress.showProgress();

        orderDetailModelClassCall.enqueue(new Callback<OrderDetailModelClass>() {
            @Override
            public void onResponse(Call<OrderDetailModelClass> call, Response<OrderDetailModelClass> response) {

                if (response.isSuccessful()){
                    txtSiteId.setText("Site Id : "+response.body().getResults().getOrder_detail().getSite_info().getId());
                    txt_created_by.setText("Created By : "+response.body().getResults().getOrder_detail().getUpdated_info().getName());
                    txt_created_date.setText("Date : "+response.body().getResults().getOrder_detail().getCreated_at());
                    txt_description.setText(response.body().getResults().getOrder_detail().getRemark());
                }
                if (response.body().getResults().getOrder_particulars().size() != 0){
                    OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(getApplicationContext(),response.body().getResults().getOrder_particulars());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    orderRecyclerView.setLayoutManager(layoutManager);
                    orderRecyclerView.setAdapter(orderDetailAdapter);
                    progress.hideProgress();
                }else{
                    Toast.makeText(OrderDetailActivity.this, "No Order data found", Toast.LENGTH_SHORT).show();
                    progress.hideProgress();
                }

                if (response.body().getResults().getOrder_venders().size() != 0){
                    if (user_type.equals("site")){
                        btnVerifyDelivery.setVisibility(View.GONE);
                        imgEdit.setVisibility(View.GONE);
                    }else if (user_type.equals("vendor")){
                        imgEdit.setVisibility(View.GONE);
                    }
                    imgEdit.setVisibility(View.GONE);
                    btnVerifyDelivery.setVisibility(View.VISIBLE);
                    VendorAdapter vendorAdapter = new VendorAdapter(getApplicationContext(),response.body().getResults().getOrder_venders());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    vendorRecyclerView.setLayoutManager(layoutManager);
                    vendorRecyclerView.setAdapter(vendorAdapter);
                }else{
                    btnVerifyDelivery.setVisibility(View.GONE);
                    imgEdit.setVisibility(View.VISIBLE);
                    Toast.makeText(OrderDetailActivity.this, "No Vendor data found", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<OrderDetailModelClass> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (orderId != null){
//            getOrderDetails(orderId);
//        }
//    }
}
