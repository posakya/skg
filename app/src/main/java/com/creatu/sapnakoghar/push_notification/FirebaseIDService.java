package com.creatu.sapnakoghar.push_notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by roshan on 6/3/17.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
//    DBHandler db;
    Context context;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,refreshedToken);
        System.out.println("RegToken : "+refreshedToken);
//        db = new DBHandler(context);
//        db.insertToken(refreshedToken);

        SharedPreferences sharedpreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("token", refreshedToken);
        editor.apply();

    }
}