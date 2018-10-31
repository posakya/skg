package com.creatu.sapnakoghar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creatu.sapnakoghar.adapter.SlidingMenuAdapter;
import com.creatu.sapnakoghar.model_class.ItemSlideMenu;
import com.creatu.sapnakoghar.side_menu.BillFragment;
import com.creatu.sapnakoghar.side_menu.Home;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private List<ItemSlideMenu> listSliding;
    RelativeLayout mainView, drawerView;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView toolbar_title,txt_userName;
    ImageView drawer_image,register_image,logOut,userImage;
    String email,username;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        txt_userName = findViewById(R.id.userName);
        //  lv_register = (ListView) findViewById(R.id.lv_register);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (RelativeLayout) findViewById(R.id.Drawer_View);
        // drawerView1 = (RelativeLayout) findViewById(R.id.Drawer_View1);
        mainView = (RelativeLayout) findViewById(R.id.mainView);
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        drawer_image = toolbar.findViewById(R.id.toolbar_image);
        userImage = findViewById(R.id.user_image);

        listSliding = new ArrayList<>();

        listSliding.add(new ItemSlideMenu(R.drawable.ic_home_black_24dp, "Orders"));
        listSliding.add(new ItemSlideMenu(R.drawable.academic, "Bills"));
        listSliding.add(new ItemSlideMenu(R.drawable.course, "Site Orders"));
        listSliding.add(new ItemSlideMenu(R.drawable.entrance, "Bill Price"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_exit_to_app_black_24dp, "LogOut"));

        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

      //Set title
    setTitle(listSliding.get(0).getTitle().replaceAll("Home", ""));
    //item selected
        listViewSliding.setItemChecked(0, true);
    //Close menu
        drawerLayout.closeDrawer(drawerView);


    //remove shadow from drawer

        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

    //Display fragment 1 when start
    replaceFragment(0);
    //Hanlde on item click

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Set title
            setTitle(listSliding.get(position).getTitle());
            //item selected
            listViewSliding.setItemChecked(position, true);
            //Replace fragment
            replaceFragment(position);
            //Close menu
            drawerLayout.closeDrawer(drawerView);

            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    });


    toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.drawer_opened, R.string.drawer_closed){
        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            mainView.setTranslationX(slideOffset * drawerView.getWidth());

            drawerLayout.bringChildToFront(drawerView);

            drawerLayout.requestLayout();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            invalidateOptionsMenu();
        }
    };

        drawerLayout.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
    // toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
        drawer_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        }
    });


}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    //Create method replace fragment

    private void replaceFragment(int pos) {
        getSupportActionBar().setTitle("");
        // Fragment fragment = null;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        switch (pos) {

            case 0:
                toolbar_title.setText("Orders");

                fragmentManager.beginTransaction().replace(R.id.main_content,new Home()).addToBackStack(null).commit();
                break;

            case 1:
                toolbar_title.setText("Bills");

                fragmentManager.beginTransaction().replace(R.id.main_content,new BillFragment()).addToBackStack(null).commit();
                break;



            case 4:

                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
                startActivity(rateIntent);

                break;

            case 5:

                Intent shareIntent =  new Intent(android.content.Intent.ACTION_SEND);

                //set type

                shareIntent.setType("text/plain");

                //add what a subject you want

                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"PdSmart Education");

                String shareMessage="https://play.google.com/store/apps/details?id=com.creatu.pdsmart";

                //message

                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareMessage);

                //start sharing via

                startActivity(Intent.createChooser(shareIntent,"Sharing via"));

                break;

            case 6:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,AlertDialog.THEME_HOLO_LIGHT).create();

                alertDialog.setTitle("PdSmart Education");
                alertDialog.setMessage(Html.fromHtml("Would you like to logout?"));

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "LogOut",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
                                sharedPreferences.edit().clear().commit();
                                register_image.setVisibility(View.VISIBLE);
                                logOut.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                dialog.dismiss();
                            }
                        });

                alertDialog.show();
                final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                final Button positveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                neutralButton.setTextColor(getResources().getColor(R.color.black));
                positveButton.setTextColor(getResources().getColor(R.color.black));
                break;

            default:

                break;

        }

    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Touch again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;

                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

}
