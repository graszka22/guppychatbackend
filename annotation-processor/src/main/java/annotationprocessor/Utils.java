package annotationprocessor;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Utils {
    public static String snakeCaseToCamelCase(String s) {
        return String.join("", Stream.of(s.split("_")).map(Utils::upperFirstLetter).toArray(String[]::new));
    }

    public static String upperFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String camelCaseToSnakeCase(String s) {
        return s.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }


    public static String getClassNameFromQualifiedName(String s) {
        return s.substring(s.lastIndexOf('.') + 1);
    }
}
