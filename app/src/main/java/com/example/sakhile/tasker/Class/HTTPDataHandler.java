package com.example.sakhile.tasker.Class;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

public class HTTPDataHandler {
    static String stream= null;

    public HTTPDataHandler(){

    }

    public void GetHTTPData(String urlString){
        try{
            URL url= new URL(urlString);
            HttpURLConnection urlConnection= (HttpURLConnection)url.openConnection();

            //check connection status
            if(urlConnection.getResponseCode()== 200){
                InputStream in= new BufferedInputStream(urlConnection.getInputStream());

                //Read the bufferInputStream
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb= new StringBuilder();
                String line;

                while((line= r.readLine()) != null){
                    sb.append(line);
                    stream= sb.toString();
                    urlConnection.disconnect();
                }
            }

            else{

            }




        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void PostHTTPData(String urlString, String json){
        try{
            URL url= new URL(urlString);
            HttpURLConnection urlConnection= (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length= out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type", "appliacation/json; charset-UTF-8");
            urlConnection.connect();

            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }

            InputStream response= urlConnection.getInputStream();



        }catch(MalformedURLException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void PutHTTPData(String urlString, String newValue){
        try{
            URL url= new URL(urlString);
            HttpURLConnection urlConnection= (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod("PUT");
            urlConnection.setDoOutput(true);

            byte[] out = newValue.getBytes(StandardCharsets.UTF_8);
            int length= out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type", "appliacation/json; charset-UTF-8");
            urlConnection.connect();

            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }

            InputStream response= urlConnection.getInputStream();



        }catch(MalformedURLException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void DeleteHTTPData(String urlString, String json){
        try{
            URL url= new URL(urlString);
            HttpURLConnection urlConnection= (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoOutput(true);

            byte[] out = json.getBytes(StandardCharsets.UTF_8);
            int length= out.length;

            urlConnection.setFixedLengthStreamingMode(length);
            urlConnection.setRequestProperty("Content-Type", "appliacation/json; charset-UTF-8");
            urlConnection.connect();

            try(OutputStream os = urlConnection.getOutputStream()){
                os.write(out);
            }

            InputStream response= urlConnection.getInputStream();



        }catch(MalformedURLException e){
            e.printStackTrace();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
