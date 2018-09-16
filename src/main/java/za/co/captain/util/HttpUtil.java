package za.co.captain.util;

import za.co.captain.exception.CaptainException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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


    public static String post(String url, String body) throws CaptainException {
        return sendWithMethod("POST", url, body);

    }

    public static String put(String url, String body) throws CaptainException {
        return sendWithMethod("PUT", url, body);

    }

    private static String sendWithMethod(String method, String url, String body) throws CaptainException {

        try {
            URL obj;
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-type", "Application/json");


            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(body);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if (responseCode != HTTP_OK) {
                throw new CaptainException(new Exception("Failed to " + method), "Http code was:" + responseCode + "\n Body:\n" + response.toString());
            }

            return response.toString();
        } catch (MalformedURLException e) {
            throw new CaptainException(e, "");
        } catch (ProtocolException e) {
            throw new CaptainException(e, "");
        } catch (IOException e) {
            throw new CaptainException(e, "");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.log(Level.INFO, url);
            LOG.log(Level.INFO, body);
            throw new CaptainException(e, "");
        }

    }
}
