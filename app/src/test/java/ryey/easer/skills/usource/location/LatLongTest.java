package ryey.easer.skills.usource.location;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.ParseException;
import java.util.Locale;


@RunWith(Parameterized.class)
public class LatLongTest {
    @Parameterized.Parameters(name = "Locale = {0}")
    public static Locale[] locales()
    {
        return new Locale[]{ Locale.US, Locale.GERMAN };
    }

    @Parameterized.Parameter
    public Locale locale;

    @Test
    public void testToFromString() throws ParseException {
            Locale.setDefault(locale);
            LatLong latLong = new LatLong(10.11, 12.13);
            Assert.assertEquals(latLong, LatLong.fromString(latLong.toString()));
    }
}
