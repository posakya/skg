package com.creatu.sapnakoghar.side_menu;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.creatu.sapnakoghar.R;
import com.creatu.sapnakoghar.adapter.NotificationAdapter;
import com.creatu.sapnakoghar.adapter.OrderAdapter;
import com.creatu.sapnakoghar.model_class.notification_model_class.NotificationModelClass;
import com.creatu.sapnakoghar.model_class.order_model_class.OrderModelClass;
import com.creatu.sapnakoghar.order_section.AddOrderActivity;
import com.creatu.sapnakoghar.progressDialog.ShowProgress;
import com.creatu.sapnakoghar.retrofit_api_client.RetrofitClient;
import com.creatu.sapnakoghar.retrofit_api_interface.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    View view;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ShowProgress progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        add_button = view.findViewById(R.id.add_button);

        add_button.setVisibility(View.GONE);

        progress = new ShowProgress(getActivity());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddOrderActivity.class));
            }
        });

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo",MODE_PRIVATE);
            String userId = sharedPreferences.getString("id",null);
            String userType = sharedPreferences.getString("user_type",null);



            if (userId != null){
                getNotifications(userId);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return view;
    }

    public void getNotifications(String userId){
        progress.showProgress();

        ApiInterface notificationInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<NotificationModelClass> notificationModelClassCall = notificationInterface.getNotification(userId);

        notificationModelClassCall.enqueue(new Callback<NotificationModelClass>() {
            @Override
            public void onResponse(Call<NotificationModelClass> call, Response<NotificationModelClass> response) {
                if (response.body().getResults().getNotifications().size() != 0){
                    NotificationAdapter adapter = new NotificationAdapter(getActivity(),response.body().getResults().getNotifications());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    progress.hideProgress();
                }else{
                    progress.hideProgress();
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationModelClass> call, Throwable t) {
                progress.hideProgress();
                Toast.makeText(getActivity(), "Response : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
