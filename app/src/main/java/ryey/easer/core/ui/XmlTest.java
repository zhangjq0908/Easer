package ryey.easer.core.ui;

import android.content.Context;
import android.util.Xml;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XmlTest {

    private final String ns = null;

    XmlTest(Context context) {
//        String str = testSerialize();
//        testParse(str);
        testJSONParse();
    }

    private void testJSONParse() {
        String data = "{\"something\":{\"O\":2}}";
        try {
            JSONObject jsonObject = new JSONObject(data);
            String str = jsonObject.optString("something");
            Logger.d(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String testSerialize() {
        XmlSerializer serializer = Xml.newSerializer();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            serializer.setOutput(out, "utf-8");
            serializer.startDocument("utf-8", false);

            serializer.startTag(ns, "mytag");
            serializer.flush();
            out.write("hahahaha".getBytes());
            serializer.endTag(ns, "mytag");

            serializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = out.toString();
        Logger.d(result);

        return result;
    }

    private void testParse(String str_in) {
        XmlPullParser parser = Xml.newPullParser();
        ByteArrayInputStream in = new ByteArrayInputStream(str_in.getBytes());

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, "mytag");
            String str = parser.nextText();
            Logger.d(str);
            parser.require(XmlPullParser.END_TAG, ns, "mytag");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
