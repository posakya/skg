package com.creatu.sapnakoghar.vendor_section;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.adapter.OrderDetailAdapter;
import com.creatu.sapnakoghar.adapter.OrderListItemAdapter;
import com.creatu.sapnakoghar.adapter.VendorAdapter;
import com.creatu.sapnakoghar.model_class.ParticularModelClass;
import com.creatu.sapnakoghar.model_class.ParticularResults;
import com.creatu.sapnakoghar.model_class.SiteResults;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.OrderDetailModelClass;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.Order_particulars;
import com.creatu.sapnakoghar.model_class.vendor_model_class.Results;
import com.creatu.sapnakoghar.model_class.vendor_model_class.VendorModelClass;
import com.creatu.sapnakoghar.order_detail.OrderDetailActivity;
import com.creatu.sapnakoghar.order_section.AddOrderActivity;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVendorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String vendor_id;
    JsonObject jsonObject;
    JsonArray particularsArray;
    ShowProgress progress;
    String siteID,userId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        recyclerView = findViewById(R.id.item_recycler_view);
        toolbar = findViewById(R.id.tool_bar);

        jsonObject = new JsonObject();
        particularsArray = new JsonArray();

        progress = new ShowProgress(AddVendorActivity.this);


        setSupportActionBar(toolbar);

        siteID = getIntent().getExtras().getString("order_id");
        String orderCode = getIntent().getExtras().getString("order_code");
        System.out.println("SiteID : "+siteID);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            userId = sharedPreferences.getString("id",null);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (siteID != null){
            getSupportActionBar().setTitle(orderCode);
            getParticular(siteID);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void submit(View view) {
        postVendor(Integer.parseInt(siteID),userId);
    }


    public void postVendor(Integer site_id,String user_id){

        progress.showProgress();

        ApiInterface addOrderInterface = RetrofitClient.getFormData().create(ApiInterface.class);


        jsonObject.addProperty("site_order_id",site_id);

        jsonObject.add("particulars",particularsArray);

        System.out.println("Particulars : "+jsonObject);


        Call<JsonObject> call = addOrderInterface.addVendor(jsonObject,user_id);

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

                            Toast.makeText(AddVendorActivity.this, results, Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (status == 400){
                            progress.hideProgress();
                            Toast.makeText(AddVendorActivity.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            progress.hideProgress();
                            Toast.makeText(AddVendorActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.hideProgress();
                    Toast.makeText(AddVendorActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.hideProgress();
            }
        });

    }

    public void getParticular(String orderId){

        ApiInterface orderDetailInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<OrderDetailModelClass> orderDetailModelClassCall = orderDetailInterface.getOrderDetails(orderId);

        progress.showProgress();

        orderDetailModelClassCall.enqueue(new Callback<OrderDetailModelClass>() {
            @Override
            public void onResponse(Call<OrderDetailModelClass> call, Response<OrderDetailModelClass> response) {


                if (response.body().getResults().getOrder_particulars().size() != 0){
                    AddVendorAdapter orderDetailAdapter = new AddVendorAdapter(getApplicationContext(),response.body().getResults().getOrder_particulars());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(orderDetailAdapter);
                    progress.hideProgress();
                }else{
                    Toast.makeText(AddVendorActivity.this, "No Order data found", Toast.LENGTH_SHORT).show();
                    progress.hideProgress();
                }


            }

            @Override
            public void onFailure(Call<OrderDetailModelClass> call, Throwable t) {
                Toast.makeText(AddVendorActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void setItem(ArrayList<Integer> itemId, final ArrayList<Integer> quantity , ArrayList<Float> price,ArrayList<Integer> vendorId){

        JsonObject jsonObject1 = new JsonObject();

        for (int i=0;i<itemId.size();i++){
            jsonObject1.addProperty("id",itemId.get(i));
            jsonObject1.addProperty("quantity",quantity.get(i));
            jsonObject1.addProperty("vender_id",vendorId.get(i));
            jsonObject1.addProperty("price",price.get(i));
        }

        particularsArray.add(jsonObject1);


    }

    public class AddVendorAdapter extends RecyclerView.Adapter<AddVendorAdapter.MyViewHolder>{

        Context context;
        private List<Order_particulars> particularResults;

        public AddVendorAdapter(Context context, List<Order_particulars> particularResults) {
            this.context = context;
            this.particularResults = particularResults;
        }

        private ArrayList<Integer> itemId = new ArrayList<>();
        private ArrayList<Integer> quantity = new ArrayList<>();
        private ArrayList<Float> price = new ArrayList<>();
        private ArrayList<Integer> vendorId1 = new ArrayList<>();

        @NonNull
        @Override
        public AddVendorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.vedor_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AddVendorAdapter.MyViewHolder holder, int position) {

            final Order_particulars particularResults1 = particularResults.get(position);


            holder.item_checkBox.setText(Html.fromHtml(particularResults1.getName()));
            ApiInterface vendorInterface = RetrofitClient.getFormData().create(ApiInterface.class);
            Call<VendorModelClass> vendorModelClassCall = vendorInterface.getVendor();

            vendorModelClassCall.enqueue(new Callback<VendorModelClass>() {
                @Override
                public void onResponse(Call<VendorModelClass> call, Response<VendorModelClass> response) {
                    if (response.body().getResults().size() != 0){

                        List<Results> vendorResults = response.body().getResults();

                        final List<String> vendorId = new ArrayList<>();
                        final List<String> vendorName = new ArrayList<>();


                        for (int i = 0; i < vendorResults.size(); i++) {

                            vendorId.add(vendorResults.get(i).getId());
                            vendorName.add(vendorResults.get(i).getName());

                            ArrayAdapter siteAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, vendorName);
                            siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                            holder.vendorSpinner.setAdapter(siteAdapter);



                        }



                        if (holder.edit_quantity.getText().toString().equals("0") || holder.edit_price.getText().toString().equals("0")){

                        }else{

                        }

                        holder.vendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                vendor_id = vendorId.get(i);





                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<VendorModelClass> call, Throwable t) {

                }
            });

            holder.item_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.edit_quantity.getText().toString().equals("0")){
                        Toast.makeText(context, "Please insert quantity", Toast.LENGTH_SHORT).show();
                        holder.item_checkBox.setChecked(false);
                    }else if (holder.edit_price.getText().toString().equals("0")){
                        Toast.makeText(context, "Please insert price", Toast.LENGTH_SHORT).show();
                        holder.item_checkBox.setChecked(false);
                    }
                    else{
                        try {

                            itemId.add(Integer.parseInt(particularResults1.getId()));
                            quantity.add(Integer.parseInt(holder.edit_quantity.getText().toString().trim()));
                            price.add(Float.parseFloat(holder.edit_quantity.getText().toString().trim()));

                            vendorId1.add(Integer.parseInt(vendor_id));


                            setItem(itemId,quantity,price,vendorId1);

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
            EditText edit_quantity,edit_price;
            AppCompatSpinner vendorSpinner;


            public MyViewHolder(View itemView) {
                super(itemView);

                item_checkBox = itemView.findViewById(R.id.item_check_box);
                edit_quantity = itemView.findViewById(R.id.edit_quantity);
                edit_price = itemView.findViewById(R.id.edit_price);
                vendorSpinner = itemView.findViewById(R.id.vendor_spinner);

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
