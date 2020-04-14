package com.talentbuilder.talentbuilder.mail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class OAuthMail {


    // For using Oauth2

    private static String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private JavaMailSender sender;

    // Not a best practice to store client id, secret and token in source
    // must be stored in a file.
    private String oauthClientId = "752744168989-lke9gb06masannrd251r2ekovk621b1k.apps.googleusercontent.com";
    private String oauthSecret = "Uhw2EBNuURe22HBpOp826tD2";
    private String refreshToken = "1//04tJkx8aKEnWYCgYIARAAGAQSNwF-L9IrPohyf4QYorkmPRficRA0n_cDaJxgzsACoS1s_LFlgjwv9uDOS9icquVsV-L2uMJ2fNg";
    private static String accessToken = "ya29.a0Ae4lvC3AZ1zMR1ojFCjVki4BPXfImI3Y5Fpg5GnxG8ujyMApahZ8JtlC5-6vQGm3CJoIMFxeCSNxntMwhJtu0r40m-vxdU817PQwjBp_Gg5Tvd3rkjTYqnzvo-312PsGT2ep70flEMfI1bwMxoQHzWh_mDvVj8dOvp0";
    private long tokenExpires = 3599L;

    // getters and setters

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        accessToken = accessToken;
    }


    /*
    Renew access token if expired
     */
    public void renewToken(){

        if(System.currentTimeMillis() > tokenExpires) {

            try
            {
                String request = "client_id="+ URLEncoder.encode(oauthClientId, "UTF-8")
                        +"&client_secret="+URLEncoder.encode(oauthSecret, "UTF-8")
                        +"&refresh_token="+URLEncoder.encode(refreshToken, "UTF-8")
                        +"&grant_type=refresh_token";
                HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(request);
                out.flush();
                out.close();
                conn.connect();

                try
                {

                    HashMap<String,Object> result;
                    result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String,Object>>() {});
                    accessToken = (String) result.get("access_token");
                    tokenExpires = System.currentTimeMillis()+(((Number)result.get("expires_in")).intValue()*1000);
                }
                catch (IOException e)
                {
                    String line;
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    while((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    System.out.flush();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}