package ryey.easer;

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
}
