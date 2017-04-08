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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    //String[] listNameCurrency;
    //ArrayAdapter<String> adapterOne, adapterTwo;
    EditText eTCurrencyOne, eTCurrencyTwo;
    TextView tvResult;
    Spinner sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*try {
            InputStream inputFile = getAssets().open("bankUaCom.xml");
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            String pCode = "", pChar3 = "", pSize = "", pName = "",
                    pRate = "", pChange = "";

            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("item");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    pCode = eElement.getElementsByTagName("code").item(0).getTextContent();
                    pChar3 = eElement.getElementsByTagName("char3").item(0).getTextContent();
                    pSize = eElement.getElementsByTagName("size").item(0).getTextContent();
                    pName = eElement.getElementsByTagName("name").item(0).getTextContent();
                    pRate = eElement.getElementsByTagName("rate").item(0).getTextContent();
                    pChange = eElement.getElementsByTagName("change").item(0).getTextContent();
                }
                Currency currencyItem = new Currency(pCode,pChar3,pSize,
                        pName,pRate,pChange);
                currencyList.add(currencyItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listNameCurrency = new String[currencyList.size()];
        for(int i = 0; i < currencyList.size(); i++)
            listNameCurrency[i] = currencyList.get(i).getChar3();

        Spinner spinnerOne = (Spinner) findViewById(R.id.spinner_currency_one);
        adapterOne = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        listNameCurrency
                );

        adapterOne.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
        spinnerOne.setAdapter(adapterOne);

        spinnerOne.setOnItemSelectedListener(new MyProcessEvent1());

        eTCurrencyOne = (EditText) findViewById(R.id.edit_text_currency_one);

        Spinner spinnerTwo = (Spinner) findViewById(R.id.spinner_currency_two);
        adapterTwo = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        listNameCurrency
                );

        adapterTwo.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
        spinnerTwo.setAdapter(adapterTwo);
        spinnerTwo.setSelection(1);

        spinnerTwo.setOnItemSelectedListener(new MyProcessEvent2());
        eTCurrencyTwo = (EditText) findViewById(R.id.edit_text_currency_two);
        */


        /*eTCurrencyOne = (EditText) findViewById(R.id.edit_text_currency_one);
        eTCurrencyTwo = (EditText) findViewById(R.id.edit_text_currency_two);
        new AsyncGetCurrencyConvert(1f,
                "CNY", "CHF", eTCurrencyTwo).execute();
        listNameCurrency = new String[]{"CNY", "CHF", "SEK", "CZK", "GBP", "HUF",
                "TRY", "KZT", "XDR", "SGD", "IDR", "RON", "RUB", "NOK", "NZD", "ILS", "MDL", "MXN",
                "HRK", "CAD", "IRR", "INR", "PLN", "JPY", "EGP", "EUR", "USD", "DKK", "HKD", "KRW",
                "BGN", "BYN", "THB", "AUD"};

        Spinner spinnerOne = (Spinner) findViewById(R.id.spinner_currency_one);
        adapterOne = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        listNameCurrency
                );

        adapterOne.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
        spinnerOne.setAdapter(adapterOne);

        spinnerOne.setOnItemSelectedListener(new MyProcessEvent1());

        Spinner spinnerTwo = (Spinner) findViewById(R.id.spinner_currency_two);
        adapterTwo = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        listNameCurrency
                );

        adapterTwo.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
        spinnerTwo.setAdapter(adapterTwo);
        spinnerTwo.setSelection(1);

        spinnerTwo.setOnItemSelectedListener(new MyProcessEvent2());*/


        //tv1 = (TextView) findViewById(R.id.textView1);

        tvResult = (TextView) findViewById(R.id.tvResult);
        sp1 = (Spinner) findViewById(R.id.spinner_currency_one);
        sp2 = (Spinner) findViewById(R.id.spinner_currency_two);
        eTCurrencyOne = (EditText) findViewById(R.id.edit_text_currency_one);
        eTCurrencyTwo = (EditText) findViewById(R.id.edit_text_currency_two);
        checkInternetConnection();
        new AsyncDownloadXMLFile(MainActivity.this, "http://bank-ua.com/export/currrate.xml", sp1, sp2,
                eTCurrencyOne, eTCurrencyTwo, tvResult).execute();
    }

    /*private class MyProcessEvent1 implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
            eTCurrencyOne.setText("25");
            currencyOneIndex = index;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class MyProcessEvent2 implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
            currencyTwoIndex = index;
            eTCurrencyTwo.setText(String.valueOf(convertCurrency(Float.parseFloat(eTCurrencyOne.getText().toString()),
                    listNameCurrency[currencyOneIndex],
                    listNameCurrency[currencyTwoIndex])));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }*/

    /*public float convertCurrency(float money, String currencyFrom, String currencyTo){
        float currencyFromRate=0f, currencyToRate = 0f;
        for(int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getChar3().equals(currencyFrom))
                currencyFromRate = Float.parseFloat(currencyList.get(i).getRate());
            if (currencyList.get(i).getChar3().equals(currencyTo))
                currencyToRate = Float.parseFloat(currencyList.get(i).getRate());
        }
        return money * currencyFromRate/currencyToRate;
    }*/

    public boolean checkInternetConnection() {
        //get Connectivity Manager object to check connection
        ConnectivityManager conn = (ConnectivityManager) getSystemService(
                getBaseContext().CONNECTIVITY_SERVICE);
        //check for network connection
        if (conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (conn.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                conn.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                conn.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                conn.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public void readFromAssests() {
        try {
            InputStream is = getAssets().open("currency.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("item");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    /*tv1.setText(tv1.getText() + "\nDate : " + getValue("date", element2) + "\n");
                    tv1.setText(tv1.getText() + "Code : " + getValue("code", element2) + "\n");
                    tv1.setText(tv1.getText() + "Char3 : " + getValue("char3", element2) + "\n");
                    tv1.setText(tv1.getText() + "-----------------------");*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
