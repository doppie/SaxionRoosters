package nl.jelletenbrinke.saxionroosters.extras;

/**
 * Created by Doppie on 4-3-2016.
 */
public class Tools {

    public static String parseQueryFromName(String name) {
        if (name.contains(" (")) {
            name = name.substring(0, name.indexOf(" ("));
        }

        return name;
    }
}
