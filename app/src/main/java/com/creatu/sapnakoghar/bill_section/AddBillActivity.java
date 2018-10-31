package com.creatu.sapnakoghar.bill_section;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.model_class.SiteModelClass;
import com.creatu.sapnakoghar.model_class.SiteResults;
import com.creatu.sapnakoghar.model_class.order_model_class.OrderModelClass;
import com.creatu.sapnakoghar.model_class.order_model_class.Orders;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBillActivity extends AppCompatActivity {

    ImageView imgBill;
    EditText editAmount;
    AppCompatSpinner site_spinner,order_spinner;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    File file1;
    String OrderCode,OrderId;
    Toolbar toolbar;
    String site_id,userId,amount,order_id;
    ShowProgress progress;
    TextView txt_orderQuantity;
    RelativeLayout rel;

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        imgBill = findViewById(R.id.addImage);
        toolbar = findViewById(R.id.tool_bar);
        site_spinner = findViewById(R.id.site_spinner);
        order_spinner = findViewById(R.id.order_spinner);
        editAmount = findViewById(R.id.edit_amount);
        txt_orderQuantity = findViewById(R.id.txt_orderQuantity);
        rel = findViewById(R.id.rel);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add bill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ShowProgress(AddBillActivity.this);

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
            userId = sharedPreferences.getString("id",null);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        OrderCode = getIntent().getExtras().getString("order_code");
        OrderId = getIntent().getExtras().getString("order_id");

        imgBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RQS_IMAGE1);
            }
        });

        if (OrderCode != null){
            txt_orderQuantity.setVisibility(View.VISIBLE);
            order_spinner.setVisibility(View.GONE);
            rel.setVisibility(View.GONE);
            txt_orderQuantity.setText(OrderCode);
            order_id = OrderId;
        }else{
            txt_orderQuantity.setVisibility(View.GONE);
            order_spinner.setVisibility(View.VISIBLE);
            rel.setVisibility(View.VISIBLE);
        }

        //// asking for permission to read the gallery

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        getAllSites();
        getAllOrders();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RQS_IMAGE1:
                    source1 = data.getData();

                    try {
                        System.out.println("Bitmap path = "+source1.getPath());
                        bm1 = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(source1));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        // bm1.compress(Bitmap.CompressFormat.PNG,100,out);
                        bm1.compress(Bitmap.CompressFormat.JPEG,70,out);
                        imgBill.setImageBitmap(bm1);

                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(source1, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();

                        file1 = new File(filePath);


                        //System.out.println("Image :"+bm1);
                        System.out.println("Image :"+file1.length());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    public void submit(View view) {
        getBillData();
    }

    public void getAllSites(){
        progress.showProgress();
        ApiInterface siteInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<SiteModelClass> siteModelClassCall = siteInterface.getSites();

        siteModelClassCall.enqueue(new Callback<SiteModelClass>() {
            @Override
            public void onResponse(Call<SiteModelClass> call, Response<SiteModelClass> response) {
                if (response.body().getResults().size() != 0){

                    progress.hideProgress();

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

    public void getAllOrders(){

        ApiInterface siteInterface = RetrofitClient.getFormData().create(ApiInterface.class);
        Call<OrderModelClass> siteModelClassCall = siteInterface.getOrders(userId,"1","10000");

        siteModelClassCall.enqueue(new Callback<OrderModelClass>() {
            @Override
            public void onResponse(Call<OrderModelClass> call, Response<OrderModelClass> response) {
                if (response.body().getResults().getOrders().size() != 0){



                    List<Orders> siteResults = response.body().getResults().getOrders();

                    final List<String> orderId = new ArrayList<>();
                    final List<String> orderCode = new ArrayList<>();


                    for (int i = 0; i < siteResults.size(); i++) {

                        orderId.add(siteResults.get(i).getId());
                        orderCode.add(siteResults.get(i).getOrder_code());


                        ArrayAdapter siteAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, orderCode);
                        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Setting the ArrayAdapter data on the Spinner

                        order_spinner.setAdapter(siteAdapter);

                    }

//                    if (orderCode.equals(OrderCode)){
//                        order_spinner.setId(Integer.parseInt(OrderCode));
//                    }

                    order_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                            order_id = orderId.get(i);



                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<OrderModelClass> call, Throwable t) {

            }
        });
    }

    public void PostBill(String userId,String order_id,String site_id,String amount, File image){
        ApiInterface addBillInterface = RetrofitClient.getFormData().create(ApiInterface.class);


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("amount",amount)
                .addFormDataPart("site_id",site_id)
                .addFormDataPart("site_order_id",order_id)
                .addFormDataPart("bill_image", image.getName(),
                        RequestBody.create(MediaType.parse("image/png"), image))
                .build();

        progress.showProgress();

        addBillInterface.addBill(requestBody,userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        int status = jsonObject.optInt("status");

                        if (status == 200){
                            String results = jsonObject.optString("results");
                            progress.hideProgress();
                            Toast.makeText(AddBillActivity.this, results, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (status == 400){
                            progress.hideProgress();
                            Toast.makeText(AddBillActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();

                        }else{
                            progress.hideProgress();
                            Toast.makeText(AddBillActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    progress.hideProgress();
                    Toast.makeText(AddBillActivity.this, "Failed to post", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.hideProgress();
            }
        });
    }

    public void getBillData(){

        amount = editAmount.getText().toString().trim();
        if (amount.equals("")){
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
        }else if (file1 == null){
            Toast.makeText(this, "Please attach bill image", Toast.LENGTH_SHORT).show();
        }else{
            PostBill(userId,order_id,site_id,amount,file1);
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
