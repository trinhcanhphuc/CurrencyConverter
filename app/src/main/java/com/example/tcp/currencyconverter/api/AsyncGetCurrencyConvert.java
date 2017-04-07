package com.example.tcp.currencyconverter.api;

import android.os.AsyncTask;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by ASUS on 3/15/2017.
 */

public class AsyncGetCurrencyConvert extends AsyncTask<Void, Void, String> {
    float currencyMoney;
    String currencyFrom;
    String currencyTo;
    EditText currencyResult;
    List<Currency> currencyListTemp = new ArrayList<>();
    String[] listNameCurrency;

    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        try {
            result = downloadUrl(getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Document doc = getDomElement(result);
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
            Currency currencyItem = new Currency(pCode, pChar3, pSize,
                    pName, pRate, pChange);
            currencyListTemp.add(currencyItem);
        }

        /*for (Currency c: currencyListTemp
             ) {
          currencyList.add(new Currency(new String (c.getCode()), new String (c.getChar3()), new String (c.getSize()), new String (c.getName()),
                  new String (c.getRate()), new String (c.getChange())));
        }
        this.currencyResult.setText(String.valueOf(convertCurrency(currencyMoney,
                currencyListTemp.get(0).getChar3(),
                currencyListTemp.get(1).getChar3())));*/

    }

    public AsyncGetCurrencyConvert(float currencyMoney, String currencyFrom, String currencyTo,
                                   EditText currencyResult) {
        this.currencyMoney = currencyMoney;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.currencyResult = currencyResult;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;


    }

    private String getUrl() {
        String url = "http://bank-ua.com/export/currrate.xml";
        return url;
    }

    public Document getDomElement(String xml) {
        Document document = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();

            is.setCharacterStream(new StringReader(xml));
            document = db.parse(is);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

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

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

}
