package com.creatu.sapnakoghar.authentication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.creatu.sapnakoghar.MainActivity;
import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;
import com.creatu.sapnakoghar.validation.Validation;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    String email, password, deviceId, fcm_token;
    TelephonyManager telephonyManager;
    int i;

    ShowProgress progress;
    SharedPreferences sharedPreferences;

    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Random random = new Random();
        i = random.nextInt(5000 - 1000)+1000;
        fcm_token = String .valueOf(i);

        telephonyManager = (TelephonyManager) getSystemService(LoginActivity.this.TELEPHONY_SERVICE);

        progress = new ShowProgress(LoginActivity.this);
        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        try {

            String name = sharedPreferences.getString("name",null);
            if (name != null){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        /*

        asking for permission to read the phone state

         */

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        deviceId = telephonyManager.getDeviceId();
        System.out.println("Device Id : "+deviceId);
        System.out.println("Device Token : "+fcm_token);

        editTextEmail = findViewById(R.id.edit_email);
        editTextPassword = findViewById(R.id.edit_password);
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

    public void signIn(View view) {
        loginData();
//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    public void postLogin(String email,String password,String fcm_token,String deviceId){

        progress.showProgress();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email",email);
        jsonObject.addProperty("password",password);
        jsonObject.addProperty("fcm_token",fcm_token);
        jsonObject.addProperty("device_id",deviceId);

        System.out.println("JsonData : "+jsonObject);

        ApiInterface loginInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<JsonObject> call = loginInterface.login(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try{
                        String response_body_string = response.body().toString();
                        System.out.println("Response Success : "+response.body().toString());

                        JSONObject jsonObject1 = new JSONObject(response_body_string);
                        int status = jsonObject1.optInt("status");

                        if (status == 200){

                            JSONObject results = jsonObject1.getJSONObject("results");

                            int id = results.optInt("id");
                            String name = results.optString("name");
                            String email = results.optString("email");
                            String user_type = results.optString("user_type");


                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id",String .valueOf(id));
                            editor.putString("name",name);
                            editor.putString("email",email);
                            editor.putString("user_type",user_type);
                            editor.apply();
                            progress.hideProgress();

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }else if (status == 400){
                            progress.hideProgress();
                            String message = jsonObject1.optString("message");
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }else{
                    progress.hideProgress();
                    Toast.makeText(LoginActivity.this, "Response Error : "+response.errorBody(), Toast.LENGTH_SHORT).show();
                    System.out.println("Response Error : "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progress.hideProgress();
                Toast.makeText(LoginActivity.this, "Response Message : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Response Message : "+t.getMessage());
            }
        });

//        JsonArray citiesArray = new JsonArray();
//        citiesArray.add("Dhaka");
//        citiesArray.add("Örebro");
//        jsonObject.add("cities", citiesArray);

    }

    public void loginData (){
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if (email.equals("")){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
        }else if (!email.matches(Validation.emailPattern)){
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }else if (password.equals("")){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }else{
            postLogin(email,password,fcm_token,deviceId);
        }
    }
}
