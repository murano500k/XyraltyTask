package com.stc.xyralitytask;

import org.apache.commons.lang.StringEscapeUtils;
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
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by artem on 1/28/18.
 */

public class LoginPresenter {


    private static final String BASE_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";
    private static final String TAG = "LoginPresenter";


    public Observable<List<String>> login(final String email, final String password) {

        return Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(BASE_URL);
                httppost.setEntity(getUrlEncodedData(email,password));
                HttpResponse response = httpclient.execute(httppost);

                return processResponse(response);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    private UrlEncodedFormEntity getUrlEncodedData(String login, String password) throws Exception {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("login", login));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("deviceId", getDevId()));
        nameValuePairs.add(new BasicNameValuePair("deviceType", getDevType()));

        return new UrlEncodedFormEntity(nameValuePairs);
    }

    private List<String> processResponse(HttpResponse response) throws IOException{
        List<String> names=new ArrayList<>();
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        while ((line = rd.readLine()) != null) {
            if(line.contains("name")){
                names.add(parseName(line));
            }
        }
        rd.close();
        return names;
    }

    private String parseName(String line) {

        //extract server name
        line=line.substring(0,line.lastIndexOf("\""));
        line=line.substring(line.lastIndexOf("\"")+1,line.length());

        //fix upper case \U
        line=line.replace("\\U","\\u");

        //convert unicode characters to readable form
        return StringEscapeUtils.unescapeJava(line);
    }


    private String getDevType() {
        return String.format("%s %s",
                android.os.Build.MODEL, android.os.Build.VERSION.RELEASE);
    }

    private String getDevId() throws SocketException {
        return String.valueOf(getMacAddr().hashCode());
    }

    private String getMacAddr() throws SocketException {
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
        return "";
    }


}
