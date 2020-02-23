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
public class TWBankLinkAsyncTask extends AsyncTask<String, Integer, String> {
    private final static String TW_BANK_URL = "https://rate.bot.com.tw/xrt?Lang=zh-TW";
    private HttpsURLConnection connection;
    private Context mContext;
    private ProgressDialog dialog;
    private ITWBankLink itwBankLink;
    private String line = "";

    public interface ITWBankLink {
        void getLinkSuccess(String data);
        void getLinkError(String data);
    }

    public TWBankLinkAsyncTask(Context mContext, ITWBankLink itwBankLink){
        this.mContext = mContext;
        this.itwBankLink = itwBankLink;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(mContext,"",mContext.getString(R.string.loading),true,false);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(TW_BANK_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(100000);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            while((line = reader.readLine()) != null){
                if(line.contains("下載 Excel (CSV) 檔")){
                    line = line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\">"));
                    break;
                }
            }

        } catch (Exception e) {
            itwBankLink.getLinkError(e.getMessage());
        }

        return "https://rate.bot.com.tw" + line;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        itwBankLink.getLinkSuccess(s);
    }
}
