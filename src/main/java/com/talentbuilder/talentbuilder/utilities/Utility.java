package com.talentbuilder.talentbuilder.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.talentbuilder.talentbuilder.constant.ServerResponseStatus;
import com.talentbuilder.talentbuilder.dto.ErrorResponse;
import com.talentbuilder.talentbuilder.dto.LoginResponse;
import com.talentbuilder.talentbuilder.dto.ServerResponse;
import com.talentbuilder.talentbuilder.dto.SignInRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Utility {

    public static boolean isValidInput(String input){
        return input != null && !input.isEmpty();
    }

    public static boolean isValidEmail(String email){
        return EmailValidator.getInstance(true).isValid(email);
    }

    public static boolean isValidPhone(String phone){
        return phone.startsWith("+234") && phone.length() > 13 || phone.startsWith("234") && phone.length() > 9 || phone.startsWith("070") && phone.length() > 9
                || phone.startsWith("080") && phone.length() > 9 || phone.startsWith("090") && phone.length() > 9
                || phone.startsWith("0") && phone.length() > 9;
    }

    public static String otp(){
        Random random = new Random();
        String numb = String.valueOf(100000 + random.nextInt(900000));
        return numb;
    }

    public static String generateRandomString(int length) {
        Random random = new Random();
        return random.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static String toDateAndTime() {
        String date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            date = format.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convert credential to basic auth Base64 String format
     * @param username
     * @param password
     * @return String
     */
    public static String getCredentials(String username, String password) {
        String plainClientCredentials = username + ":" + password;
        String base64ClientCredentials = new String(org.apache.commons.codec.binary.Base64.encodeBase64(plainClientCredentials.getBytes()));

        String authorization = "Basic " + base64ClientCredentials;
        return authorization;
    }

    public static ServerResponse loginHttpRequest(String urlPath, SignInRequest request, String authorization) {
        try {
            ServerResponse response = new ServerResponse();

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer()).create();

            String input  = "grant_type=" + URLEncoder.encode(request.getGrant_type()) +
                    "&username=" + URLEncoder.encode(request.getUsername()) +
                    "&password=" + URLEncoder.encode(request.getPassword()) ;

            StringEntity postingString = new StringEntity(input);

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }};

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
            }


            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(urlPath);
            post.setEntity(postingString);
            post.addHeader("Content-type", "application/x-www-form-urlencoded");
            post.addHeader("Authorization", authorization);
            StringBuilder result = new StringBuilder();
            HttpResponse responseData = client.execute(post);

            if (responseData.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(responseData.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                LoginResponse loginResponse = gson.fromJson(result.toString(), LoginResponse.class);

                if (loginResponse.getUser() != null && !loginResponse.getUser().isActive()) {

                    response.setData("");
                    response.setMessage("Activation require, please activate your account");
                    response.setSuccess(false);
                    response.setStatus(ServerResponseStatus.FAILED);
                    return response;
                }

                if (loginResponse.getUser() != null && loginResponse.getUser().isLock()) {

                    response.setData("");
                    response.setMessage("Your account has been locked, please contact support");
                    response.setSuccess(false);
                    response.setStatus(ServerResponseStatus.FAILED);
                    return response;
                }

                response.setData(loginResponse);
                response.setMessage("Login Successful");
                response.setSuccess(true);
                response.setStatus(responseData.getStatusLine().getStatusCode());
                return response;
            } else {
                BufferedReader rd = new BufferedReader(new InputStreamReader(responseData.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                ErrorResponse errorResponse = gson.fromJson(result.toString(), ErrorResponse.class);

                if (errorResponse.getError_description().contains("Bad credentials")){
                    response.setMessage("Invalid email and password");
                }else {
                    response.setMessage(errorResponse.getError_description());
                }
                response.setStatus(responseData.getStatusLine().getStatusCode());
                response.setData("");
                response.setSuccess(false);
                response.setStatus(ServerResponseStatus.FAILED);
                return response;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
