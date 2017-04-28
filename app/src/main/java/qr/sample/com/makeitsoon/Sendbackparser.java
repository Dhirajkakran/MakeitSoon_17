package qr.sample.com.makeitsoon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dhiraj.kumar on 2/28/2017.
 */

public class Sendbackparser {
    ArrayList<String> myaeslist;
    JSONObject jsonArray;
    JSONArray jsoobject, arrayjson, jsonArrayval;
    JSONArray jsonArraylast;



    public JSONObject getJsonArrayFromURL(String url, String value11) {
        InputStream inputStream = null;
        URL urlPost;
        HttpURLConnection connection = null;
        try {



            // String values = jsonObject.toString();
            String dataUrlParameters = "=" + value11;
            // Create connection1`
            urlPost = new URL(url);
            connection = (HttpURLConnection) urlPost.openConnection();
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(dataUrlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

// Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            //wr.write(dataUrlParameters.getBytes("UTF-8"));
            wr.writeBytes(dataUrlParameters);
            wr.flush();
            wr.close();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(connection.getInputStream());
                String response = convertInputStreamToString(inputStream);
                try {
                  /*  ArrayList myaeslist = aes.getallkey();
                    String value = response.split(":")[0].toString().replace("\"", "");
                    String key1 = myaeslist.get(Integer.parseInt(value) - 1).toString().split(":")[1].toString();
                    String result=response.split(":")[1].toString().replace("'\'", "");
                    String check = aes.Decrypt(result, key1);
                    String feedback = check.toString().replaceAll("\'", "").trim();*/
                    jsonArray = new JSONObject(response.trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        if (null != inputStream) {
            inputStream.close();
        }
        return result;
    }

    public ArrayList<mobAboutMetaData>  parseVector(String url, String value,mobAboutMetaData data1) {
        JSONObject jsonObject1=null;
        ArrayList<mobAboutMetaData> list=null;
        try {
            jsonObject1 = getJsonArrayFromURL(url, value);
            if(jsonObject1!=null){
                list= new ArrayList<>();
                JSONArray jsonArray=jsonObject1.getJSONArray("DeviceDistanceList");
                if((jsonArray!=null)&&(jsonArray.length()>0))
                {
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mobAboutMetaData data = new mobAboutMetaData();
                        data.setLat(""+jsonObject.getString("Latitude"));
                        data.setLon("" + jsonObject.getString("Longitude"));

                        data.setCity(jsonObject.getString("City"));
                        data.setState(jsonObject.getString("State"));
                        data.setPin(jsonObject.getString("pincode"));
                        data.setDeviceid(jsonObject.getString("DeviceId"));
                        data.setDis(jsonObject.getString("DistanceInMeter"));


                        list.add( data);
                    }
                    list.add(data1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }
}
