package rumstajn.githubcommitviewer;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Date;

public class Util {
    public static void makeToast(String msg, Activity activity){
        activity.runOnUiThread(() -> {
            Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        });
    }

    public static String shrinkString(String string, int maxChars){
        if (string.length() <= maxChars){
            return string;
        }
        return string.substring(0, maxChars - 3) + "...";
    }

    public static int clampValueToMax(int value, int max){
        return value <= max ? value : max;
    }

    public static String readHTTPInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder buff = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            buff.append(line);
            buff.append("\n");
        }
        reader.close();
        return buff.toString();
    }

    public static long getRateLimitExpirationTime(HttpURLConnection connection) throws ParseException {
        String rateLimitResetRaw = connection.getHeaderField("x-ratelimit-reset");
        if (rateLimitResetRaw == null){
            return -1;
        }

        Date expiry = new Date(Long.parseLong(rateLimitResetRaw) * 1000);
        Date now = new Date();

        long diff = Math.abs(expiry.getTime() - now.getTime());
        return diff / (60 * 1000) % 60; // minutes
    }
}
