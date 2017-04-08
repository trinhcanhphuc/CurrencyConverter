package com.example.tcp.currencyconverter;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tcp.currencyconverter.api.AsyncDownloadXMLFile;

public class MainActivity extends AppCompatActivity {

    EditText eTCurrencyOne, eTCurrencyTwo;
    TextView tvResult;
    Spinner sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        sp1 = (Spinner) findViewById(R.id.spinner_currency_one);
        sp2 = (Spinner) findViewById(R.id.spinner_currency_two);
        eTCurrencyOne = (EditText) findViewById(R.id.edit_text_currency_one);
        eTCurrencyTwo = (EditText) findViewById(R.id.edit_text_currency_two);
        //checkInternetConnection();
        //if mobile connected internet then download xml file, update database and display
        new AsyncDownloadXMLFile(MainActivity.this, "http://bank-ua.com/export/currrate.xml", sp1, sp2,
                eTCurrencyOne, eTCurrencyTwo, tvResult, checkInternetConnection()).execute();
    }

    public boolean checkInternetConnection() {
        //get Connectivity Manager object to check connection
        ConnectivityManager conn = (ConnectivityManager) getSystemService(
                getBaseContext().CONNECTIVITY_SERVICE);
        //check for network connection
        if (conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            Toast.makeText(this, " Connected Internet", Toast.LENGTH_LONG).show();
            return true;
        } else if (conn.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                conn.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
            Toast.makeText(this, " Not Connected Internet", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
