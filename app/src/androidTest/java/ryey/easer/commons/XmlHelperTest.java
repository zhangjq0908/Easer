package ryey.easer.commons;

import android.util.Xml;

import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.core.data.storage.backend.xml.event.C;

import static org.junit.Assert.assertEquals;

public class XmlHelperTest {
    String ns = null;

    XmlPullParser parser = Xml.newPullParser();
    XmlSerializer serializer = Xml.newSerializer();
    ByteArrayInputStream in;
    ByteArrayOutputStream out;

    @Before
    public void setUp() throws Exception {
        in = new ByteArrayInputStream("<tag1>my data</tag1>".getBytes());
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        out = new ByteArrayOutputStream();
        serializer.setOutput(out, "utf-8");
    }

    @Test
    public void testGetText() throws Exception {
        while (parser.next() != XmlPullParser.START_TAG);
        assertEquals(XmlPullParser.START_TAG, parser.getEventType());
        assertEquals(XmlHelper.getText(parser, "mytag"), "my data");
    }

    @Test
    public void testSingleSituation() throws Exception {
        String spec = "ispec";
        String at = "iat"; //Data
        String expectedData = String.format("<situation spec=\"%s\"><at>%s</at></situation>", spec, at);
        XmlHelper.EventHelper.writeSingleSituation(serializer, spec, at);
        serializer.flush();
        String data = out.toString();
        assertEquals(expectedData, data);
        in = new ByteArrayInputStream(data.getBytes());
        parser.setInput(in, null);
        while (parser.next() != XmlPullParser.START_TAG);
        assertEquals(ryey.easer.core.data.storage.backend.xml.event.C.SIT, parser.getName());
        assertEquals(spec, parser.getAttributeValue(ns, C.SPEC));
        assertEquals(at, XmlHelper.EventHelper.readSingleSituation(parser));
    }

    @Test
    public void testLogic() throws Exception {
        String template = "<logic>%s</logic>";
        for (EventType type : EventType.values()) {
            out = new ByteArrayOutputStream();
            serializer.setOutput(out, "utf-8");
            XmlHelper.EventHelper.writeLogic(serializer, type);
            serializer.flush();
            String data = out.toString();
            assertEquals(String.format(template, type), data);
            in = new ByteArrayInputStream(data.getBytes());
            parser.setInput(in, null);
            EventType ret = XmlHelper.EventHelper.readLogic(parser);
            assertEquals(type, ret);
        }
    }
}