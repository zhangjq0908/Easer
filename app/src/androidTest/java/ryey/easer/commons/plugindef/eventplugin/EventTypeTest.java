package ryey.easer.commons.plugindef.eventplugin;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import ryey.easer.R;

import static org.junit.Assert.*;

public class EventTypeTest {

    Context context = InstrumentationRegistry.getTargetContext();

    @Test
    public void desc() throws Exception {
        String[] strings = context.getResources().getStringArray(R.array.event_type);
        EventType[] types = EventType.values();
        assertEquals(strings.length, types.length);
        for (int i = 0; i < strings.length; i++)
            assertEquals(strings[i], types[i].desc(context));
    }

}