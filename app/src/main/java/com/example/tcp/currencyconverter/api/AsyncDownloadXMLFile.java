package com.example.tcp.currencyconverter.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by tcp on 4/7/17.
 */

public class AsyncDownloadXMLFile extends AsyncTask<Void , String, String> {

    private Context context;
    private String f_url = "";
    private TextView tvResult;
    private ProgressDialog progressDialog;

    public List<Currency> currencyList = new ArrayList<>();
    String[] listNameCurrency;
    ArrayAdapter<String> adapterOne, adapterTwo;
    Spinner spinnerOne, spinnerTwo;
    int currencyOneIndex, currencyTwoIndex;
    EditText eTCurrencyOne, eTCurrencyTwo;
    private DBHelper mydb;

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //showDialog(progress_bar_type);
    }

    public AsyncDownloadXMLFile(Context context, String f_url, Spinner spinnerOne, Spinner spinnerTwo,
    EditText eTCurrencyOne, EditText eTCurrencyTwo, TextView tvResult){
        this.context = context;
        this.f_url = f_url;
        this.spinnerOne = spinnerOne;
        this.spinnerTwo = spinnerTwo;
        this.eTCurrencyOne = eTCurrencyOne;
        this.eTCurrencyTwo = eTCurrencyTwo;
        this.tvResult = tvResult;
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(Void... params) {
        int count;
        try {
            URL url = new URL(f_url);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = connection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = context.openFileOutput("currency.xml",MODE_WORLD_READABLE);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return null;
    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        //readFromInternal("currency.xml");
        readFromDatabase("currency.xml");
    }

    public void readFromDatabase(String path) {
        try {
            FileInputStream fIn = context.openFileInput(path);
            InputStream is = fIn;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("item");

            String pName = "", pFName = "", pRate = "";

            mydb = new DBHelper(context);

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    pName = eElement.getElementsByTagName("char3").item(0).getTextContent();
                    pFName = eElement.getElementsByTagName("name").item(0).getTextContent();
                    pRate = eElement.getElementsByTagName("rate").item(0).getTextContent();
                }

                mydb.insertCurrency(pName, "", pRate);
                Cursor rs = mydb.getData(i + 1);
                rs.moveToFirst();
                pName = rs.getString(rs.getColumnIndex(DBHelper.CURRENCY_COLUMN_NAME));
                pFName = rs.getString(rs.getColumnIndex(DBHelper.CURRENCY_COLUMN_FNAME));
                pRate = rs.getString(rs.getColumnIndex(DBHelper.CURRENCY_COLUMN_RATE));

                Currency currencyItem = new Currency(pName, pFName, pRate);
                currencyList.add(currencyItem);
            }

            listNameCurrency = new String[currencyList.size()];
            for(int i = 0; i < currencyList.size(); i++)
                listNameCurrency[i] = currencyList.get(i).getChar3();

            adapterOne = new ArrayAdapter<String>
                    (
                            context,
                            android.R.layout.simple_spinner_item,
                            listNameCurrency
                    );
            adapterTwo = new ArrayAdapter<String>
                    (
                            context,
                            android.R.layout.simple_spinner_item,
                            listNameCurrency
                    );

            adapterOne.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
            spinnerOne.setAdapter(adapterOne);

            adapterTwo.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
            spinnerTwo.setAdapter(adapterTwo);

            spinnerOne.setOnItemSelectedListener(new MyProcessEvent1());
            spinnerTwo.setOnItemSelectedListener(new MyProcessEvent2());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromInternal(String path) {
        try {
            //InputStream is = getAssets().open("currency.xml");

            FileInputStream fIn = context.openFileInput(path);
            InputStream is = fIn;
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("item");

            String pCode = "", pChar3 = "", pSize = "", pName = "",
                    pRate = "", pChange = "";

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    //tvResult.setText(tvResult.getText() + "Char3 : " + getValue("char3", element2) + "\n");

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

            listNameCurrency = new String[currencyList.size()];
            for(int i = 0; i < currencyList.size(); i++)
                listNameCurrency[i] = currencyList.get(i).getChar3();

            adapterOne = new ArrayAdapter<String>
                    (
                            context,
                            android.R.layout.simple_spinner_item,
                            listNameCurrency
                    );
            adapterTwo = new ArrayAdapter<String>
                    (
                            context,
                            android.R.layout.simple_spinner_item,
                            listNameCurrency
                    );

            adapterOne.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
            spinnerOne.setAdapter(adapterOne);

            adapterTwo.setDropDownViewResource (android.R.layout.simple_list_item_single_choice);
            spinnerTwo.setAdapter(adapterTwo);

            spinnerOne.setOnItemSelectedListener(new MyProcessEvent1());
            spinnerTwo.setOnItemSelectedListener(new MyProcessEvent2());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    public float convertCurrency(float money, String currencyFrom, String currencyTo){
        float currencyFromRate=0f, currencyToRate = 0f;
        for(int i = 0; i < currencyList.size(); i++) {
            if (currencyList.get(i).getChar3().equals(currencyFrom))
                currencyFromRate = Float.parseFloat(currencyList.get(i).getRate());
            if (currencyList.get(i).getChar3().equals(currencyTo))
                currencyToRate = Float.parseFloat(currencyList.get(i).getRate());
        }
        return money * currencyFromRate/currencyToRate;
    }

    private class MyProcessEvent1 implements AdapterView.OnItemSelectedListener
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
    }

}

