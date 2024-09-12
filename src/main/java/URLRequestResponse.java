import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class URLRequestResponse {

    public static URL generateURL(String shop, String method, String token) {
        String shopNumber = shop;
        String methodNumber = method;
        String dataAPI = null;
        String dataMethod = null;
        if (shopNumber.equals("wb")) {
            if (methodNumber.equals("stocks")) {
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/stocks?dateFrom=" + getDateCurrent() + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("sales")) {
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/sales?dateFrom=" + getDate(-7) + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("orders")) {
                dataAPI = "https://statistics-api.wildberries.ru";
                dataMethod = "/api/v1/supplier/orders?dateFrom=" + getDate(-7) + "T00%3A00%3A00.000Z&key=" + token;
            }
            if (methodNumber.equals("info")) {
                dataAPI = "https://discounts-prices-api.wb.ru";
                dataMethod = "/api/v2/list/goods/filter?limit=1000&offset=0";
            }
            if (methodNumber.equals("prices")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/prices";
            }
            if (methodNumber.equals("updateDiscounts")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/updateDiscounts";
            }
            if (methodNumber.equals("updatePromocodes")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/public/api/v1/updatePromocodes";
            }
            if (methodNumber.equals("getList")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/content/v1/cards/cursor/list";
            }
            if (methodNumber.equals("getCard")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/content/v2/get/cards/list";
            }
            if (methodNumber.equals("warehouses")) {
                dataAPI = "https://suppliers-api.wildberries.ru";
                dataMethod = "/api/v1/warehouses";
            }
            if (methodNumber.equals("options")) {
                dataAPI = "https://supplies-api.wildberries.ru/";
                dataMethod = "/api/v1/acceptance/options?quantity=5&barcode=2040764686667";
            }
//            if (methodNumber.equals("getRating")) {
//                dataAPI = "https://feedbacks-api.wildberries.ru";
//                dataMethod = "/api/v1/feedbacks/products/rating/nmid" + "?" + arrayList.get(0).getKey() + "=" + arrayList.get(0).getData();
//            }
        } else if (shopNumber.equals("ozon")) {
            dataAPI = "https://api-seller.ozon.ru";
            if (methodNumber.equals("list"))
                dataMethod = "/v2/product/list";
            if (methodNumber.equals("info"))
                dataMethod = "/v2/product/info";
            if (methodNumber.equals("fbo/list"))
                dataMethod = "/v2/posting/fbo/list";
            if (methodNumber.equals("import/prices"))
                dataMethod = "/v1/product/import/prices";
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

    public static String getResponseFromURLandBodyRequest(URL url, String token, String supplierArticle) throws IOException {

        String reqBody = "";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", token);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
//        reqBody = "{\"settings\":{\"cursor\"}[\"" + supplierArticle + "\"],\"allowedCategoriesOnly\":true}";
//        reqBody = "{\"settings\":{\"sort\":{\"ascending\": false}, \"filter\": {\"textSearch\": \"\", \"allowedCategoriesOnly\": true, \"tagIDs\": [ ], \"objectIDs\": [ ], \"brands\": [ ], \"imtID\": " + supplierArticle + " , \"withPhoto\": -1}, \"cursor\": {\"updatedAt\": \"\", \"nmID\": 0, \"limit\": 11} } }";
        reqBody = "{\"settings\":{\"cursor\": {\"limit\": 100},\"filter\":{\"withPhoto\": -1}}}";
        writer.write(reqBody);
        writer.close();

        System.out.println(reqBody);

        return getResponse(httpURLConnection);
    }

//    public static String getResponseFromURLandBodyRequest(URL url, String token, String parametr0, String parametr1, String parametr2) throws IOException {
//
//        String reqBody = "";
//
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setRequestProperty("accept", "application/json");
//        httpURLConnection.setRequestProperty("Authorization", token);
//        httpURLConnection.setDoOutput(true);
//        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
//        reqBody = "{\"vendorCodes\":[\"" + supplierArticle + "\"],\"allowedCategoriesOnly\":true}";
//        writer.write(reqBody);
//        writer.close();
//
//        System.out.println(reqBody);
//
//        return getResponse(httpURLConnection);
//    }

    public static String getResponseFromURL(URL url, String token) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", token);
        httpURLConnection.setDoOutput(true);

        return getResponse(httpURLConnection);
    }

    private static String getResponse(HttpURLConnection httpURLConnection) throws IOException {
        try {
            String in = httpURLConnection.getHeaderField(0);
            System.out.println(in);
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }


    public static String getResponseFromURL(URL url, String token, String client, String method, String product_id, String price) throws IOException, URISyntaxException {

        String methodNumber = method;
        String reqBody = "";

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Client-Id", client);
        httpURLConnection.setRequestProperty("Api-Key", token);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
        if (methodNumber.equals("list"))
            reqBody = "{\"filter\":{\"visibility\": \"ALL\"},\"last_id\": \"\", \"limit\": 100}";
        if (methodNumber.equals("info"))
            reqBody = "{\"offer_id\": \"\",\"product_id\": " + product_id + ", \"sku\": 0}";
        if (methodNumber.equals("fbo/list"))
            reqBody = "{\"dir\": \"ASC\", \"filter\": {\"since\": \"" + getDateCurrent() + "T00:00:00.000Z\"}, \"limit\": 5, \"offset\": 0, \"translit\": true, \"with\": {\"analytics_data\": true, \"financial_data\": true}}";
        if (methodNumber.equals("import/prices"))
            reqBody = "{\"prices\": [{\"auto_action_enabled\": \"UNKNOWN\",\"min_price\": \"100\", \"offer_id\": \"\", \"old_price\": \"0\", \"price\": \"" + price + "\", \"product_id\": " + product_id + "}]}";
        writer.write(reqBody);
        writer.close();

        System.out.println(reqBody);

        return getResponse(httpURLConnection);
    }
}
