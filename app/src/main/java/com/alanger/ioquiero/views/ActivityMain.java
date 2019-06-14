package com.alanger.ioquiero.views;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import com.alanger.ioquiero.views.fragments.FragmentCambiarContrasenha;
import com.alanger.ioquiero.views.fragments.FragmentPedidos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.alanger.ioquiero.getTariff.view.FragmentTariff;
import com.alanger.ioquiero.views.fragments.FragmentSelectFinishMap;
import com.alanger.ioquiero.R;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentTariff.OnFragmentInteractionListener,
        FragmentPedidos.OnFragmentInteractionListener,
        FragmentCambiarContrasenha.OnFragmentInteractionListener
        {

    private Fragment myFragment = null;

    public static final String TAG = ActivityMain.class.getSimpleName();


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myFragment = new FragmentTariff(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_tarifario) {
            myFragment = new FragmentTariff(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();
        } else if (id == R.id.nav_mispedidos) {
            myFragment = new FragmentPedidos(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();
        } else if (id == R.id.nav_contrasenha) {
            myFragment = new FragmentCambiarContrasenha(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myFragment).commit();
        } else if (id == R.id.nav_share) {


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
