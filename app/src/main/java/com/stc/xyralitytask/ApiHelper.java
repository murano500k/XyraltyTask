package com.stc.xyralitytask;

import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artem on 1/25/18.
 */

public class ApiHelper {
    private static final String BASE_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";
    private static final String TAG = "ApiHelper";

    List<String> postData(String login, String password, String devid, String devType) {
        List<String> result=new ArrayList<>();
        Log.d(TAG, "postData() called with: login = [" + login +
                "], password = [" + password + "], devid = [" + devid +
                "], devType = [" + devType + "]");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("login", login));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("deviceId", devid));
            nameValuePairs.add(new BasicNameValuePair("deviceType", devType));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            Log.d(TAG, "postData: before post");
            HttpResponse response = httpclient.execute(httppost);
            result=readJsonStream(response.getEntity().getContent());
            /*
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            String all="";


            while ((line = rd.readLine()) != null) {
                if(line.contains("name")){
                    String name = parseName(line);
                    String converted = decode(name);
                    result.add(converted);
                }
                all+=line+"\n";


            }
            rd.close();
            Log.w(TAG, "postData:\n\n"+all );
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private String decode(final String in)
    {
        String working = in;
        int index;
        index = working.indexOf("\\U");
        while(index > -1)
        {
            int length = working.length();
            if(index > (length-6))break;
            int numStart = index + 2;
            int numFinish = numStart + 4;
            String substring = working.substring(numStart, numFinish);
            int number = Integer.parseInt(substring,16);
            String stringStart = working.substring(0, index);
            String stringEnd   = working.substring(numFinish);
            working = stringStart + ((char)number) + stringEnd;
            index = working.indexOf("\\U");
        }
        return working;
    }


    private String parseName(String line) {
        String res = line.substring(line.indexOf("=")+3);
        res=res.substring(0,res.length()-2);
        return res;
    }


    public List<String> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        reader.setLenient(true);
        reader.beginObject();
        while(!reader.nextName().equals("allAvailableWorlds")){
            reader.skipValue();
        }


        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<String> readMessagesArray(JsonReader reader) throws IOException {
        String text = null;

        List<String> messages = new ArrayList<String>();

        reader.beginArray();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                text = reader.nextString();
                messages.add(text);
            } else {
                reader.skipValue();
            }
        }
        reader.endArray();
        return messages;
    }

}
