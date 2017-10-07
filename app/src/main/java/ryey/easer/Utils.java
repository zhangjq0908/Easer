package ryey.easer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Utils {
    public static boolean isBlank(String str) {
        if (str == null)
            return true;
        if (str.isEmpty())
            return true;
        if (str.trim().isEmpty())
            return true;
        return false;
    }

    public static Set<Integer> str2set(String text) throws ParseException {
        Set<Integer> days = new HashSet<>();
        for (String str : text.split("\n")) {
            if (isBlank(str))
                continue;
            days.add(Integer.parseInt(str));
        }
        return days;
    }

    public static String set2str(Set<Integer> days) {
        String str = "";
        for (int day : days) {
            str += String.format(Locale.US, "%d\n", day);
        }
        return str;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, String.format(
                    context.getString(R.string.prompt_prevented_for_permission), permission),
                    Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }
}
