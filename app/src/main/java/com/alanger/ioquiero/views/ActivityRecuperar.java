package com.alanger.ioquiero.views;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.alanger.ioquiero.R;

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
      /*  GraphqlClient.getMyApolloClient().query(
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
        });*/

    }

    public void Login(String email,String password) {
   /*     GraphqlClient.getMyApolloClient()
                .query(
                    LoginQuery
                            .builder()
                            .email(email)
                            .pass(password)
                            .build()
                    )
                .enqueue(new ApolloCall.Callback<LoginQuery.Data>() {

                    @Override
                    public void onResponse(@Nonnull Response<LoginQuery.Data> response) {

                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {

                    }
                });
*/
    }



}
