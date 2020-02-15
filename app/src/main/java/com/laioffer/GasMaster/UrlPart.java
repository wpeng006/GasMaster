package com.laioffer.GasMaster;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.laioffer.GasMaster.Utility.DIRECTION_PREFIX;
import static com.laioffer.GasMaster.Utility.GOOGLE_MAP_API;

public class UrlPart {

  /**
   * A method to get url from source address and destination address.
   */
  public static String getUrl(String source, String dest) {
    String strUrl = DIRECTION_PREFIX + "origin=" + source + "&destination=" + dest + "&key=" + GOOGLE_MAP_API;
    return strUrl;
  }

  /**
   * A method to download json data from url.
   */
  public static String downloadUrl(String strUrl) throws IOException {
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try {
      URL url = new URL(strUrl);

      urlConnection = (HttpURLConnection) url.openConnection();

      urlConnection.connect();

      iStream = urlConnection.getInputStream();

      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

      StringBuffer sb = new StringBuffer();

      String line = "";
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

      data = sb.toString();

      br.close();

    } catch (Exception e) {
      Log.d("Exception", e.toString());
    } finally {
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }
}
