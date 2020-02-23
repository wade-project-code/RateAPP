package com.wade.rateapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ActMain extends AppCompatActivity {
    private String link = "";
    private TextView mTxtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getTWBankLink();
    }

    private void init(){
        mTxtContent = findViewById(R.id.mTxtContent);
    }

    private void getTWBankLink(){
        new TWBankLinkAsyncTask(this, new TWBankLinkAsyncTask.ITWBankLink() {
            @Override
            public void getLinkSuccess(String data) {
                link = data;
                getTWBankData();
            }

            @Override
            public void getLinkError(String data) {
            }
        }).execute();
    }

    private void getTWBankData(){
        new TWBankDataAsyncTask(this, new TWBankDataAsyncTask.ITWBankData() {
            @Override
            public void getDataSuccess(final String data) {
                mTxtContent.setText(data);
            }

            @Override
            public void getDataError(String data) {
            }
        }).execute(link);
    }
}
