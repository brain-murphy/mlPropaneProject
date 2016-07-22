package algorithms;

import java.util.Map;
import java.util.Objects;

public interface Algorithm {
    @Deprecated
    void setParams(Map<String, Object> params);

    void setParams(Params params);

    public static class Params {}

    public static class Info {}
}
