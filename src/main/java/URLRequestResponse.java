import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class URLRequestResponse {

    public static URL generateURL(int shop, int method, String token) {
        int shopNumber = shop;
        int methodNumber = method;
        String dataAPI = null;
        String dataMethod = null;
        switch (shopNumber){
            case (2):
                dataAPI = "https://suppliers-api.wildberries.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/public/api/v1/info?quantity=0";
                        break;
                    case (2):
                        dataMethod = "/public/api/v1/prices";
                        break;
                    case (3):
                        dataMethod = "/public/api/v1/updateDiscounts";
                        break;
                    case (4):
                        dataMethod = "/public/api/v1/updatePromocodes";
                        break;
                    case (5):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/stocks?dateFrom=" + getDateCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    case (6):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/sales?dateFrom=" + getDateCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    case (7):
                        dataAPI = "https://suppliers-stats.wildberries.ru";
                        dataMethod = "/api/v1/supplier/orders?dateFrom=" + getDateCurrent() + "T00%3A00%3A00.000Z&key=" + token;
                        break;
                    default:
                        break;
                }
                break;
            case (1):
                dataAPI = "https://suppliers-api.wildberries.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/public/api/v1/info?quantity=0";
                        break;
                    case (2):
                        dataMethod = "/public/api/v1/prices";
                        break;
                    case (3):
                        dataMethod = "/public/api/v1/updateDiscounts";
                        break;
                    case (4):
                        dataMethod = "/public/api/v1/updatePromocodes";
                        break;
                    default:
                        break;
                }
                break;
            case (3):
                dataAPI = "https://api-seller.ozon.ru";
                switch (methodNumber){
                    case (1):
                        dataMethod = "/v2/product/list";
                        break;
                    case (2):
                        dataMethod = "/v2/product/info";
                        break;
                    case (3):
                        dataMethod = "/v2/posting/fbo/list";
                        break;
                    case (4):
                        dataMethod = "/v1/product/import/prices";
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        URL url = null;
        try {
            url = new URL(dataAPI + dataMethod);
            System.out.println(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getDateCurrent() {
        Date date = new Date();
        String str = date.toString();
        String[] subStr;
        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода split()
        String day = subStr[2];
        String month = subStr[1];
        String year = subStr[5];
        String month1 = month;
        if (month.equals("Jan")) month1 = "01";
        else if (month.equals("Feb")) month1 = "02";
        else if (month.equals("Mar")) month1 = "03";
        else if (month.equals("Apr")) month1 = "04";
        else if (month.equals("May")) month1 = "05";
        else if (month.equals("Jun")) month1 = "06";
        else if (month.equals("Jul")) month1 = "07";
        else if (month.equals("Aug")) month1 = "08";
        else if (month.equals("Sep")) month1 = "09";
        else if (month.equals("Oct")) month1 = "10";
        else if (month.equals("Nov")) month1 = "11";
        else month1 = "12";
        return year + "-" + month1 + "-" + day;
    }

    public static String getDate(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i);
        String str = calendar.getTime().toString();
        String[] subStr;
        String delimeter = " "; // Разделитель
        subStr = str.split(delimeter); // Разделения строки str с помощью метода split()
        String day = subStr[2];
        String month = subStr[1];
        String year = subStr[5];
        String month1 = month;
        if (month.equals("Jan")) month1 = "01";
        else if (month.equals("Feb")) month1 = "02";
        else if (month.equals("Mar")) month1 = "03";
        else if (month.equals("Apr")) month1 = "04";
        else if (month.equals("May")) month1 = "05";
        else if (month.equals("Jun")) month1 = "06";
        else if (month.equals("Jul")) month1 = "07";
        else if (month.equals("Aug")) month1 = "08";
        else if (month.equals("Sep")) month1 = "09";
        else if (month.equals("Oct")) month1 = "10";
        else if (month.equals("Nov")) month1 = "11";
        else month1 = "12";
        return year + "-" + month1 + "-" + day;
    }

    public static String getResponseFromURL(URL url, String token) throws IOException, URISyntaxException {
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestProperty("accept", "application/json");
//        httpURLConnection.setRequestProperty("Authorization", token);
//        try {
//            InputStream in = httpURLConnection.getInputStream();
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//            boolean hasInput = scanner.hasNext();
//            if (hasInput) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            httpURLConnection.disconnect();
//        }

//        System.out.println(url);
//        System.out.println(token);

        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpGet httpGet = new HttpGet(url.toURI());
        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accept", "application/json"));
        params.add(new BasicNameValuePair("Authorization", token));
        httpGet.setHeader("accept", "application/json");
        httpGet.setHeader("Authorization", token);

        try (
                CloseableHttpResponse response2 = httpclient.execute(httpGet)
        ){
            final HttpEntity entity2 = response2.getEntity();
            String respond = EntityUtils.toString(entity2);
            return respond;
        }
    }

    public static String getResponseFromURL(URL url, String token, String client, int method, String product_id, String price) throws IOException, URISyntaxException {

        String reqBody = "";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Client-Id", client);
        httpURLConnection.setRequestProperty("Api-Key", token);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
        if (method == 1) reqBody = "{\"filter\":{\"visibility\": \"ALL\"},\"last_id\": \"\", \"limit\": 100}";
        if (method == 2) reqBody = "{\"offer_id\": \"\",\"product_id\": " + product_id + ", \"sku\": 0}";
//        if (method == 3) reqBody = "{\"dir\": \"ASC\", \"filter\": {\"since\": \"" + getDataCurrent() + "T00:00:00.000Z\"}, \"limit\": 5, \"offset\": 0, \"translit\": true, \"with\": {\"analytics_data\": true, \"financial_data\": true}}";
        if (method == 4) reqBody = "{\"prices\": [{\"auto_action_enabled\": \"UNKNOWN\",\"min_price\": \"100\", \"offer_id\": \"\", \"old_price\": \"0\", \"price\": \"" + price + "\", \"product_id\": " + product_id + "}]}";
        if (method == 3) reqBody = "{\"dir\": \"ASC\", \"filter\": {\"since\": \"2022-05-29T00:00:00.000Z\"}, \"limit\": 5, \"offset\": 0, \"translit\": true, \"with\": {\"analytics_data\": true, \"financial_data\": true}}";
        System.out.println(reqBody);
        writer.write(reqBody);
        writer.close();
        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return  null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }



    public static String getResponseFromURL(URL url) throws IOException, URISyntaxException {
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        try {
//            InputStream in = httpURLConnection.getInputStream();
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//            boolean hasInput = scanner.hasNext();
//            if (hasInput) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            httpURLConnection.disconnect();
//        }
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        final HttpUriRequest httpGet = new HttpGet(url.toURI());
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet))

        {
            final HttpEntity entity1 = response1.getEntity();
            String respond = EntityUtils.toString(entity1);
            return respond;
        }
    }

    public static String getResponseFromURL(URL url, String key0, String data, String key1, int value, String token) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", token);
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
        String reqBody = "[{\"" + key0 + "\":" + data + ",\"" + key1 + "\":" + value + "}]";
        writer.write(reqBody);
        writer.close();
        try {
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return  null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
