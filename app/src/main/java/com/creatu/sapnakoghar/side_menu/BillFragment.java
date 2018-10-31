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
import com.creatu.sapnakoghar.adapter.BillAdapter;
import com.creatu.sapnakoghar.adapter.OrderAdapter;
import com.creatu.sapnakoghar.bill_section.AddBillActivity;
import com.creatu.sapnakoghar.model_class.bill_model_class.BillModelClass;
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
public class BillFragment extends Fragment {


   View view;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ShowProgress progress;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        add_button = view.findViewById(R.id.add_button);

        progress = new ShowProgress(getActivity());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddBillActivity.class));
            }
        });

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserInfo",MODE_PRIVATE);
            String  userId = sharedPreferences.getString("id",null);
            String userType = sharedPreferences.getString("user_type",null);

//            if (userType.equals("vendor")){
//                add_button.setVisibility(View.GONE);
//            }

            if (userId != null){
                getBillOrders(userId);
            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return  view;
    }

    public void getBillOrders(String userId){

        progress.showProgress();

        ApiInterface orderInterface = RetrofitClient.getFormData().create(ApiInterface.class);

        Call<BillModelClass> orderModelClassCall = orderInterface.getBills(userId,"1","100000");

        orderModelClassCall.enqueue(new Callback<BillModelClass>() {
            @Override
            public void onResponse(Call<BillModelClass> call, Response<BillModelClass> response) {
                if (response.body().getResults().getOrder_bills().size() != 0){
                    BillAdapter adapter = new BillAdapter(getActivity(),response.body().getResults().getOrder_bills());
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
            public void onFailure(Call<BillModelClass> call, Throwable t) {
                progress.hideProgress();
                Toast.makeText(getActivity(), "Response : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
