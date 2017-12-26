package ryey.easer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventData;

public class Utils {
    public static boolean isBlank(@Nullable String str) {
        if (str == null)
            return true;
        if (str.isEmpty())
            return true;
        if (str.trim().isEmpty())
            return true;
        return false;
    }

    public static <T extends EventData> boolean eEquals(@NonNull T obj1, @NonNull T obj2) {
        return obj1.type() == obj2.type();
    }

    public static boolean nullableEqual(@Nullable Object obj1, @Nullable Object obj2) {
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
        StringBuilder str = new StringBuilder();
        for (int day : days) {
            str.append(String.format(Locale.US, "%d\n", day));
        }
        return str.toString();
    }

    public static <T> List<String> set2strlist(Set<T> set) {
        List<String> list = new ArrayList<>(set.size());
        for (T num : set) {
            list.add(num.toString());
        }
        return list;
    }

    public static String StringListToString(List<String> category) {
        StringBuilder text = new StringBuilder();
        if (category != null) {
            for (String line : category) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty())
                    text.append(trimmed).append('\n');
            }
        }
        return text.toString();
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

    public static int checkedIndexFirst(CompoundButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isChecked())
                return i;
        }
        throw new IllegalStateException("At least one button should be checked");
    }
}
