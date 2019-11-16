package com.alanger.ioquiero.volskayaGraphql;

import com.alanger.ioquiero.type.CustomType;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class GraphqlClient {

    private static CustomTypeAdapter<Double> doubleCustomTypeAdapter  = new CustomTypeAdapter<Double>(){
        @Override
        public Double decode(@NotNull CustomTypeValue customTypeValue) {
            return Double.valueOf(customTypeValue.value.toString());
        }

        @NotNull
        @Override
        public CustomTypeValue encode(@NotNull Double value) {
            return new CustomTypeValue.GraphQLNumber(value);
        }
    };


    private static final String BASE_URL = "https://volskayaforce.herokuapp.com/api/graphql";
    public static ApolloClient getMyApolloClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.DOUBLE, doubleCustomTypeAdapter)
                .build();
        return apolloClient;
    }
}
