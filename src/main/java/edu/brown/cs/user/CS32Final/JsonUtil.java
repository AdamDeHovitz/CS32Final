package edu.brown.cs.user.CS32Final;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * A utility class that converts an object to JSON.
 */
public final class JsonUtil {

    /**
     * Constructor.
     */
    private JsonUtil() {
    }

    /**
     * Converts the given object to JSON.
     *
     * @param object
     *            : Object
     * @return a String
     */
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    /**
     * For Spark.
     *
     * @return toJson() function.
     */
    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}
