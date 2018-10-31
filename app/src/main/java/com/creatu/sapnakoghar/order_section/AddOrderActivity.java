package com.creatu.sapnakoghar.order_section;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.creatu.sapnakoghar.MainActivity;
import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.adapter.OrderListItemAdapter;
import com.creatu.sapnakoghar.model_class.CheckedOrder;
import com.creatu.sapnakoghar.model_class.ParticularModelClass;
import com.creatu.sapnakoghar.model_class.ParticularResults;
import com.creatu.sapnakoghar.model_class.SiteModelClass;
import com.creatu.sapnakoghar.model_class.SiteResults;
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

public class AddOrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edit_desc;
    AppCompatSpinner site_spinner;
    RecyclerView recyclerView;
    String site_Name,locationName,description;
    TextView txtLocation;
    List<CheckedOrder> selectedItem;
    String site_id,userId;
    ArrayList<Integer> itemId1;
    ArrayList<Integer> quantity1;
    JsonObject jsonObject;
    JsonArray particularsArray;

    ShowProgress progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        toolbar = findViewById(R.id.tool_bar);
        edit_desc = findViewById(R.id.edit_desc);
        site_spinner = findViewById(R.id.site_spinner);
        recyclerView = findViewById(R.id.item_recycler_view);
        txtLocation = findViewById(R.id.txt_location);

        selectedItem = new ArrayList<>();
        jsonObject = new JsonObject();
        particularsArray = new JsonArray();

        progress = new ShowProgress(AddOrderActivity.this);

        itemId1 = new ArrayList<>();
        quantity1 = new ArrayList<>();

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getAllSites();
        getParticular();

        try {
           SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            userId = sharedPreferences.getString("id",null);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public void submit(View view) {

       getOrderData();

    }

    public void getOrderData(){
        description = edit_desc.getText().toString().trim();

        if (description.equals("")){
            Toast.makeText(this, "Please put some description!!", Toast.LENGTH_SHORT).show();
        }else{
            postOrder(description,userId);
        }
    }

    public void postOrder(String description,String user_id){

        progress.showProgress();

        ApiInterface addOrderInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        jsonObject.addProperty("description",description);
        jsonObject.addProperty("site_id",site_id);



        jsonObject.add("particulars",particularsArray);

        System.out.println("Particulars : "+jsonObject);


        Call<JsonObject> call = addOrderInterface.addOrder(jsonObject,user_id);

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

                            Toast.makeText(AddOrderActivity.this, results, Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (status == 400){
                            progress.hideProgress();
                            Toast.makeText(AddOrderActivity.this, message, Toast.LENGTH_SHORT).show();
                        }else{
                            progress.hideProgress();
                            Toast.makeText(AddOrderActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.hideProgress();
                    Toast.makeText(AddOrderActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.hideProgress();
            }
        });

    }

    public void getParticular(){

        progress.showProgress();

        ApiInterface particularInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<ParticularModelClass> particularModelClassCall = particularInterface.getParticularItem();

        particularModelClassCall.enqueue(new Callback<ParticularModelClass>() {
            @Override
            public void onResponse(Call<ParticularModelClass> call, Response<ParticularModelClass> response) {
                if (response.body().getResults().size() !=0){
                    OrderListItemAdapter adapter = new OrderListItemAdapter(getApplicationContext(),response.body().getResults());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    progress.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<ParticularModelClass> call, Throwable t) {
                progress.hideProgress();
                Toast.makeText(AddOrderActivity.this, "Response "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAllSites(){
        ApiInterface siteInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<SiteModelClass> siteModelClassCall = siteInterface.getSites();

        siteModelClassCall.enqueue(new Callback<SiteModelClass>() {
            @Override
            public void onResponse(Call<SiteModelClass> call, Response<SiteModelClass> response) {
                if (response.body().getResults().size() != 0){

                    List<SiteResults> siteResults = response.body().getResults();

                    final List<String> siteId = new ArrayList<>();
                    final List<String> siteName = new ArrayList<>();
                    final List<String> location = new ArrayList<>();

                    for (int i = 0; i < siteResults.size(); i++) {

                        siteId.add(siteResults.get(i).getId());
                        siteName.add(siteResults.get(i).getName());
                        location.add(siteResults.get(i).getLocation());

                        ArrayAdapter siteAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, siteName);
                        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Setting the ArrayAdapter data on the Spinner

                        site_spinner.setAdapter(siteAdapter);

                    }

                    site_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                            site_id = siteId.get(i);
                            locationName = location.get(i);
                            txtLocation.setText(Html.fromHtml(locationName));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SiteModelClass> call, Throwable t) {

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



    public void setItem(ArrayList<Integer> itemId, final ArrayList<Integer> quantity){

        JsonObject jsonObject1 = new JsonObject();

        for (int i=0;i<itemId.size();i++){
            jsonObject1.addProperty("id",itemId.get(i));
            jsonObject1.addProperty("quantity",quantity.get(i));
        }

        particularsArray.add(jsonObject1);


    }

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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

            final ParticularResults results = particularResults.get(position);

            holder.item_checkBox.setText(Html.fromHtml(results.getName()));
            holder.edit_quantity.getText().toString();
            holder.edit_quantity.setText("0");

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


}
