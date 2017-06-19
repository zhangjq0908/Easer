package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

class IntentData {
    String action;
    List<String> category;
    String type;
    Uri data;

    static String categoryToString(List<String> category) {
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

    static List<String> stringToCategory(String text) {
        List<String> list = new ArrayList<>();
        for (String str : text.split("\n")) {
            String trimmed = str.trim();
            if (!trimmed.isEmpty())
                list.add(trimmed);
        }
        return list;
    }

    @Override
    public String toString() {
        return String.format("action:%s category:%s type:%s data:%s", action, category, type, data);
    }
}
