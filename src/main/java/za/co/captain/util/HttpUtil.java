package za.co.captain.util;

import za.co.captain.exception.CaptainException;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtil {

    private static final String USER_AGENT = "Postgrest Java Client";
    private static final Logger LOG = Logger.getLogger(HttpUtil.class.getName());
    private static final int HTTP_OK = 200;

    public static String get(String url) throws CaptainException {

        LOG.log(Level.FINE, "GET " + url);

        try {
            URL obj;
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            if (responseCode != HTTP_OK) {
                throw new CaptainException(new Exception("Failed to get."), "Http code was:" + responseCode);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (MalformedURLException e) {
            throw new CaptainException(e, "");
        } catch (ProtocolException e) {
            throw new CaptainException(e, "");
        } catch (IOException e) {
            throw new CaptainException(e, "");
        }

    }

    public static String delete(String url) throws CaptainException {

        try {
            URL obj;
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            if (responseCode != HTTP_OK) {
                throw new CaptainException(new Exception("Failed to delete."), "Http code was:" + responseCode);
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (MalformedURLException e) {
            throw new CaptainException(e, "");
        } catch (ProtocolException e) {
            throw new CaptainException(e, "");
        } catch (IOException e) {
            throw new CaptainException(e, "");
        } catch (Exception e) {
            throw new CaptainException(e, "");
        }

    }


    public static String post(String url, Object item) throws CaptainException {
        return sendWithMethod("POST", url, item);

    }

    public static String put(String url, Object item) throws CaptainException {
        return sendWithMethod("PUT", url, item);

    }

    private static String sendWithMethod(String method, String path, Object item) throws CaptainException {

        try {

            URL url = new URL(path);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(60000);

            JsonUtil.objectMapper.writeValue(connection.getOutputStream(), item);
            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED && connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("URL: " + path);
                System.err.println("Object:");
                System.err.println(JsonUtil.objectToJson(item));

                throw new RuntimeException("Failed : HTTP error code : "
                        + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connection.getInputStream())));

            StringBuilder out = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                out.append(output);
            }

            connection.disconnect();
            return out.toString();
        } catch (SocketTimeoutException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new CaptainException(ex, "Timeout!");
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new CaptainException(ex, "");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new CaptainException(ex, "");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new CaptainException(ex, "");
        }

    }

    private static InputStream getInputStream(HttpsURLConnection con) {
        try {
            return con.getInputStream();
        } catch (IOException e) {
            //TODO: might not matter - see spec of Postgrest
        }
        return null;
    }
}
