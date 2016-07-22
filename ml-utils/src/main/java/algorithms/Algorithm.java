package algorithms;

import java.util.Map;
import java.util.Objects;

public interface Algorithm {
    void setParams(Map<String, Object> params);

    public static class Properties {}

    public static class Info {}
}
