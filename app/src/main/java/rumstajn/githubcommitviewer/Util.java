package rumstajn.githubcommitviewer;

import android.content.Context;
import android.widget.Toast;

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
}