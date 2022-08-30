package zinsoft.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonUnixTimestampDateSerializer extends JsonSerializer<Object> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int unixTime = 0;

        if (value instanceof Integer) {
            unixTime = ((Integer) value).intValue();
        } else if (value instanceof String) {
            unixTime = Integer.parseInt((String) value);
        }

        Date date = new Date(unixTime * 1000L);
        String formattedDate = dateFormat.format(date);

        gen.writeString(formattedDate);
    }

}
