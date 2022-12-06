package tool.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @className: JsonUtil
 * @author: Lying
 * @description: TODO
 * @date: 2022/12/6 上午10:08
 */
@SuppressWarnings(value = "unused")
public class JsonUtil {
    private final static LogUtil log = new LogUtil(JsonUtil.class);

    public static <T> T fromJson(String jsonString, Class<T> classOfT) {
        return fromJson(jsonString, classOfT, false);
    }

    public static <T> T fromJson(String jsonString, Class<T> classOfT, boolean isUpperCamelCase) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setFieldNamingPolicy(
                            isUpperCamelCase ? FieldNamingPolicy.UPPER_CAMEL_CASE : FieldNamingPolicy.IDENTITY)
                    .create();
            return gson.fromJson(jsonString, classOfT);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("JSON字符串转换到 JsonObject 对象错误");

            return null;
        }
    }

    public static Map<Object, Object> fromJson(String jsonString) {
        Type type = new TypeToken<Map<Object, Object>>() {
        }.getType();

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(type, new MapTypeAdapter()) // 保持数字格式，不强制转换为float
                    .serializeNulls() // 支持输出NULL
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE) // 首字母大写
                    .create();
            return gson.fromJson(jsonString, type);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("JSON字符串转换到 JsonObject 对象错误");

            return null;
        }
    }

    public static <T> String toUpperCaseJson(T t) {
        Type type = new TypeToken<Map<Object, Object>>() {
        }.getType();

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(type, new MapTypeAdapter()) // 保持数字格式，不强制转换为float
                    .serializeNulls() // 支持输出NULL
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)  // 首字母大写
                    .setPrettyPrinting()
                    .create();
            return gson.toJson(t);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("对象转换到JSON字符串出现错误");

            return null;
        }
    }

    public static <T> String toJson(T t) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls() // 支持输出NULL
                    .setPrettyPrinting()
                    .create();
            return gson.toJson(t);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("对象转换到JSON字符串出现错误");

            return null;
        }
    }

    public static <T> String toJsonExPretty(T t) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls() // 支持输出NULL
                    .create();
            return gson.toJson(t);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("对象转换到JSON字符串出现错误");

            return null;
        }
    }

    public static <T> String toUpperCaseJsonExPretty(T t) {
        Type type = new TypeToken<Map<Object, Object>>() {
        }.getType();

        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(type, new MapTypeAdapter()) // 保持数字格式，不强制转换为float
                    .serializeNulls() // 支持输出NULL
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)  // 首字母大写
                    .create();
            return gson.toJson(t);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("对象转换到JSON字符串出现错误");

            return null;
        }
    }

    private static void mergeInto(final JsonObject o1, final JsonObject o2) {
        for (final Map.Entry<String, JsonElement> e : o2.entrySet()) {
            o1.add(e.getKey(), e.getValue());
        }
    }

    public static JsonObject merge(final JsonObject o1, final JsonObject o2) {
        final JsonObject merged = new JsonObject();
        mergeInto(merged, o1);
        mergeInto(merged, o2);
        return merged;
    }

    public static <T> T fromJsonIgnoreUpperAndLower(String jsonString, Class<T> classOfT) {
        JsonObject jsonObject = JsonUtil.fromJson(jsonString, JsonObject.class);

        List<Field> fields = new ArrayList<>(Arrays.asList(classOfT.getDeclaredFields()));

        Class<?> supperClassOfT = classOfT.getSuperclass();

        while (supperClassOfT != null) {
            fields.addAll(Arrays.asList(supperClassOfT.getDeclaredFields()));

            supperClassOfT = supperClassOfT.getSuperclass();
        }

        for (Field field : fields) {
            String fieldName = field.getName();

            if (!jsonObject.has(fieldName)) {
                String upperFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                if (jsonObject.has(upperFieldName)) {
                    JsonElement value = jsonObject.get(upperFieldName);

                    jsonObject.remove(upperFieldName);
                    jsonObject.add(fieldName, value);
                } else {
                    String lowerFieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

                    if (jsonObject.has(lowerFieldName)) {
                        JsonElement value = jsonObject.get(lowerFieldName);

                        jsonObject.remove(lowerFieldName);
                        jsonObject.add(fieldName, value);
                    }
                }
            }
        }

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        return gson.fromJson(JsonUtil.toJson(jsonObject), classOfT);
    }
}
