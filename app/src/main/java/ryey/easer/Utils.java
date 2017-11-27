package ryey.easer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public static boolean nullableEqual(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null)
            return true;
        if (obj1 == null || obj2 == null)
            return false;
        return obj1.equals(obj2);
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

    public static <T> List<String> set2strlist(Set<T> set) {
        List<String> list = new ArrayList<>(set.size());
        for (T num : set) {
            list.add(num.toString());
        }
        return list;
    }

    public static String StringListToString(List<String> category) {
        String text = "";
        if (category != null) {
            for (String line : category) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty())
                    text += trimmed + '\n';
            }
        }
        return text;
    }

    public static List<String> stringToStringList(String text) {
        List<String> list = new ArrayList<>();
        for (String str : text.split("\n")) {
            String trimmed = str.trim();
            if (!trimmed.isEmpty())
                list.add(trimmed);
        }
        return list;
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
