package com.example.tcp.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tcp.currencyconverter.api.Currency;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    List<Currency> currencyList = new ArrayList<>();
    String[] listNameCurrency;
    ArrayAdapter<String> adapterOne, adapterTwo;
    EditText eTCurrencyOne, eTCurrencyTwo;
    int currencyOneIndex, currencyTwoIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
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
                    currencyList.get(currencyOneIndex).getChar3(),
                    currencyList.get(currencyTwoIndex).getChar3())));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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
}
