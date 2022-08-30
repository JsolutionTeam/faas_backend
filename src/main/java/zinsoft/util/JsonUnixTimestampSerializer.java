package zinsoft.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonUnixTimestampSerializer extends JsonSerializer<Integer> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(Integer unixTime, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Date date = new Date(unixTime * 1000L);
        String formattedDate = dateFormat.format(date);

        gen.writeString(formattedDate);
    }

}
