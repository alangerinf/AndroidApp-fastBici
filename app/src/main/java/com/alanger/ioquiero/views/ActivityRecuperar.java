package com.alanger.ioquiero.views;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alanger.ioquiero.AllUsersQuery;
import com.alanger.ioquiero.R;
import com.alanger.ioquiero.VolskayaGraphqlClient;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

public class ActivityRecuperar extends Activity {

    private static final String TAG = "VolskayaGraphQlServer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        testVolskayaGraphql();
    }


    public void testVolskayaGraphql(){
        VolskayaGraphqlClient.getMyApolloClient().query(
                AllUsersQuery.builder().build()).enqueue(new ApolloCall.Callback<AllUsersQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AllUsersQuery.Data> response) {
                List<AllUsersQuery.AllUser> users = response.data().allUsers();
                for(AllUsersQuery.AllUser user : users) {
                    Log.d(TAG, "onResponse email: " + user. email());
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {

            }
        });

    }



}
