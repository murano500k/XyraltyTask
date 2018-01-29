package com.stc.xyralitytask;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by artem on 1/28/18.
 */

public class LoginPresenter {


    public Single<List<String>> login(final String email, final String password){
        return Single.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                return postDataAsync(email,password);
            }
        }).subscribeOn(Schedulers.newThread());

    }
    private String getDevType() {
        return String.format("%s %s",
                android.os.Build.MODEL, android.os.Build.VERSION.RELEASE);
    }

    private String getDevId() {
        return String.valueOf(getMacAddr().hashCode());
    }
    private String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
    private static final String BASE_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";
    private static final String TAG = "ApiHelper";

    List<String> postDataAsync(String login, String password) {
        List<String> result=new ArrayList<>();
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(BASE_URL);

        try {
            httppost.setEntity(getUrlEncodedData(login,password));
            Log.w(TAG, "getURI: "+httppost.getURI() );
            HttpResponse response = httpclient.execute(httppost);
            result= processResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> processResponse(HttpResponse response) throws IOException{
        List<String> names=new ArrayList<>();


        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        String all="";


        while ((line = rd.readLine()) != null) {
            if(line.contains("name")){
                String name = parseName(line);
                String converted = decode(name);
                names.add(converted);
            }
            all+=line+"\n";


        }
        rd.close();
        Log.w(TAG, "postData:\n\n"+all );
        return null;
    }

    private UrlEncodedFormEntity getUrlEncodedData(String login, String password) throws UnsupportedEncodingException {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("login", login));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("deviceId", getDevId()));
        nameValuePairs.add(new BasicNameValuePair("deviceType", getDevType()));

        return new UrlEncodedFormEntity(nameValuePairs);
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
        for(String s: line.split("\"")){
            Log.d(TAG, line+" split : [" + s + "]");
        }
        String res = line.substring(line.indexOf("=")+3);
        res=res.substring(0,res.length()-2);
        return res;
    }
}
