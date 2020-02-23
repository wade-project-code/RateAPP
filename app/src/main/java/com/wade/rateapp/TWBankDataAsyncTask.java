package com.wade.rateapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Wade on 2020/2/23.
 */
public class TWBankDataAsyncTask extends AsyncTask<String, Integer, String> {
    private HttpsURLConnection connection;
    private ITWBankData itwBankData;
    private Context mContext;
    private ProgressDialog dialog;
    private StringBuilder sb = new StringBuilder();

    public interface ITWBankData{
        void getDataSuccess(String data);
        void getDataError(String data);
    }

    public TWBankDataAsyncTask(Context mContext, ITWBankData itwBankData){
        this.mContext = mContext;
        this.itwBankData = itwBankData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(mContext,"",mContext.getString(R.string.loading),true,false);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            if(connection.getResponseCode() != 200){
                itwBankData.getDataError(connection.getResponseMessage());
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = "";

            while((line = reader.readLine()) != null){
                sb.append(line);
            }

        } catch (Exception e) {
            itwBankData.getDataError(e.getMessage());
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        itwBankData.getDataSuccess(s);
    }
}
