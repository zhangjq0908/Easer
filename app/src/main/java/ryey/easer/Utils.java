package ryey.easer;

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
}
