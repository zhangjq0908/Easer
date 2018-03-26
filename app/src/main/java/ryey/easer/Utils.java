package ryey.easer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventData;

public class Utils {

    public static void panic(String message, Object... objs) {
        Logger.e(message, objs);
        throw new IllegalStateException(String.format(message, objs));
    }

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

    public static String StringCollectionToString(@NonNull Collection<String> collection, boolean trailing) {
        StringBuilder text = new StringBuilder();
        boolean is_first = true;
        for (String line : collection) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                if (!is_first)
                    text.append("\n");
                text.append(trimmed);
                is_first = false;
            }
        }
        if (!is_first && trailing)
            text.append("\n");
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

    private static final String F_DATE = "%DATE%";
    private static final String F_TIME = "%TIME%";

    private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-DD", Locale.US);
    private static final SimpleDateFormat sdf_time = new SimpleDateFormat("HH-mm-ss", Locale.US);

    public static String format(String format_string) {
        if (format_string == null)
            return null;
        Date now = Calendar.getInstance().getTime();
        String res = format_string;
        res = res.replaceAll(F_DATE, sdf_date.format(now));
        res = res.replaceAll(F_TIME, sdf_time.format(now));
        return res;
    }
}
