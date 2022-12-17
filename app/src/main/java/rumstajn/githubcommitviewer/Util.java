package rumstajn.githubcommitviewer;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Util {
    public static void makeToast(String msg, Context context){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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
}
