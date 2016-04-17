package dev.saxionroosters.extras;

import android.os.Build;
import android.util.Log;

import com.lapism.searchview.adapter.SearchItem;

import java.util.ArrayList;
import java.util.Set;

import dev.saxionroosters.R;
import dev.saxionroosters.model.Group;
import dev.saxionroosters.model.Owner;
import dev.saxionroosters.model.Result;
import dev.saxionroosters.model.Setting;
import dev.saxionroosters.model.Teacher;

/**
 * Created by Doppie on 4-3-2016.
 */
public class Tools {

    public static boolean logging = false;

    public static boolean isLollipop() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }
        return false;
    }

    public static String parseQueryFromName(String name) {
        if (name.contains(" (")) {
            name = name.substring(0, name.indexOf(" ("));
        }

        return name;
    }

    public static Owner.OwnerType parseOwnerTypeFromString(String type) {
        if(type == null) return null;
        if (type.equals(S.TEACHER)) return Owner.OwnerType.TEACHER;
        if (type.equals(S.GROUP)) return Owner.OwnerType.GROUP;
        if (type.equals(S.COURSE)) return Owner.OwnerType.COURSE;
        if (type.equals(S.ACADEMY)) return Owner.OwnerType.ACADEMY;

        return null;
    }

    public static String getDividerStringForOwnerType(Owner.OwnerType type) {
        if (type.equals(Owner.OwnerType.GROUP)) return "Groepen";
        if (type.equals(Owner.OwnerType.COURSE)) return "Opleidingen";
        if (type.equals(Owner.OwnerType.ACADEMY)) return "Academies";
        if (type.equals(Owner.OwnerType.TEACHER)) return "Docenten";

        return S.UNKNOWN;
    }

    public static int getCountForOwnerType(Owner.OwnerType type, ArrayList<Result> results) {
        int count = 0;

        for (Result result : results) {
            if (parseOwnerTypeFromString(result.getType()).equals(type)) count++;
        }

        return count;
    }

    public static ArrayList<Owner> getResultsForOwnerAdapter(ArrayList<Result> results, boolean enableDividers) {
        ArrayList<Owner> searchResults = new ArrayList<>();
        ArrayList<Owner.OwnerType> searchDividers = new ArrayList<>();

        for (Result result : results) {
            Owner owner = null;
            Owner.OwnerType type = Tools.parseOwnerTypeFromString(result.getType());

            if (enableDividers && !searchDividers.contains(type) && isOwnerTypeAccepted(type)) {
                String dividerText = Tools.getDividerStringForOwnerType(type);
                dividerText += " - " + Tools.getCountForOwnerType(type, results) + " resultaten";
                searchResults.add(new Owner(dividerText, null));
                searchDividers.add(type);
            }

            owner = createOwnerForResult(result, type);

            if (owner != null && isOwnerTypeAccepted(type)) searchResults.add(owner);
        }

        return searchResults;
    }

    public static Owner createOwnerForResult(Result result, Owner.OwnerType type) {
        Owner owner = null;
        if(type.equals(Owner.OwnerType.GROUP)) {
            owner = new Group(result.getAbbrevation(), result.getName());
        } else if(type.equals(Owner.OwnerType.TEACHER)) {
            owner = new Teacher(result.getName(), result.getAbbrevation());
        }

        return owner;
    }

    public static ArrayList<SearchItem> getResultsForSearchView(ArrayList<Result> results) {
        ArrayList<SearchItem> searchResults = new ArrayList<>();

        for(Result result : results) {
            SearchItem item = null;
            if(result.getName() != null && !result.getName().isEmpty()) {
                item = new SearchItem(result.getAbbrevation() + " (" + result.getName() + ")", R.drawable.magnify_grey);
            } else if(result.getAbbrevation() != null) {
                item = new SearchItem(result.getAbbrevation(), R.drawable.magnify_grey);
            }

            Owner.OwnerType type = Tools.parseOwnerTypeFromString(result.getType());
            if(item != null && isOwnerTypeAccepted(type)) searchResults.add(item);
        }

        return searchResults;
    }

    public static boolean isOwnerTypeAccepted(Owner.OwnerType type) {
        if (type.equals(Owner.OwnerType.GROUP)) return true;
        if (type.equals(Owner.OwnerType.TEACHER)) return true;
        if (type.equals(Owner.OwnerType.COURSE)) return false;
        if (type.equals(Owner.OwnerType.ACADEMY)) return false;


        //unknown is always false.
        return false;
    }

    public static String getOwnerRepresentativeName(Owner owner) {
        if(owner instanceof Group) return ((Group) owner).getName();
        if(owner instanceof Teacher) return ((Teacher) owner).getName();

        return owner.getName();
    }

    public static String getOwnerIdName(Owner owner) {
        if(owner instanceof Group) return ((Group) owner).getName();
        if(owner instanceof Teacher) return ((Teacher) owner).getIdName();

        return owner.getName();
    }

    public static void log(String message) {
        if(logging) Log.e("SaxionRoosters", message);
    }
}
