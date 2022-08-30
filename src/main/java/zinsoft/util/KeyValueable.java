package zinsoft.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface KeyValueable<T> {

    @JsonIgnore
    public T getKey();

    @JsonIgnore
    public String getValue();

}
