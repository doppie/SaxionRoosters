package nl.jelletenbrinke.saxionroosters.extras;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import nl.jelletenbrinke.saxionroosters.model.College;
import nl.jelletenbrinke.saxionroosters.model.Day;
import nl.jelletenbrinke.saxionroosters.model.Group;
import nl.jelletenbrinke.saxionroosters.model.Owner;
import nl.jelletenbrinke.saxionroosters.model.Result;
import nl.jelletenbrinke.saxionroosters.model.Teacher;
import nl.jelletenbrinke.saxionroosters.model.Week;

/**
 * Created by Doppie on 22-2-2016.
 * This class contains all static methods regarding parsing .
 */
public class HtmlParser {


    /**
     * This method parses html to get a single week with its days and colleges.
     * @param doc The html page that should be parsed.
     * @param week the simple week object without the days.
     * @return full week object.
     */
    public static Week parseWeek(Document doc, Week week) {
        ArrayList<Day> days = new ArrayList<>();
        Day currentDay = null;

        //Every table contains a day of the week.
        //Every day contains colleges for that day.
        Elements tables = doc.select("table");
        for (Element table : tables) {
            ArrayList<College> colleges = new ArrayList<>();

            //below are the vars we need for a college.
            String times = null;
            String collegeName = null;
            String location = null;
            String collegeType = null;
            ArrayList<Teacher> teachers = new ArrayList<>();

            Elements tableRows = table.select("tr");
            for (Element row : tableRows) {
                //in this loop we will parse the colleges


                //Table rows contain table headers and table data
                //So we loop them both.
                Elements tableHeaders = row.select("th");
                Elements tableData = row.select("td");

                for (Element th : tableHeaders) {
                    //Top of the daytable so create a new day object!
                    //this header contains the name of the day.
                    if (th.className().equals("tabletop")) {
                        if (currentDay != null) days.add(currentDay);

                        currentDay = new Day(th.text());
                    }

                    //Top of the college so start a new college!
                    //a college always starts with times.
                    else if (th.className().equals(S.TIMES)) {

                        //save the current college and reset vars
                        if (times != null) {
                            College college = new College(collegeName, collegeType, times, location, teachers);
                            currentDay.getColleges().add(college);

                            collegeName = null;
                            collegeType = null;
                            times = null;
                            location = null;
                            teachers = new ArrayList<>();
                        }

                        //set times
                        times = th.text();
                    } else if (th.className().equals(S.NAMEROW)) {
                        //The namerow (college name) contains multiple spans with different info.
                        //Luckily they are all classed except for the college name which is just a basic span.
                        Elements thSpans = th.select("span");
                        for (Element span : thSpans) {
                            if (span.className().isEmpty()) {
                                collegeName = span.text();
                            }
                        }
                    }
                }

                for (Element td : tableData) {
                    Elements tdSpans = td.select("span");
                    for (Element span : tdSpans) {
                        //this one actually contains a string with all teachers
                        //Right now we put them all in one teacher object
                        //TODO: give every Teacher its own Object.
                        if (span.className().equals(S.DATACELL_LEFT)) {
                            Element link = span.firstElementSibling();
                            Teacher teacher = new Teacher(link.text());
                            teachers.add(teacher);
                        } else if (span.className().equals(S.PULL_RIGHT)) {
                            //The location contains a string with all locations
                            //So maybe we should create an arraylist of this one.
                            //TODO: create location Objects and put them in arraylist.
                            location = span.text();
                        } else if (span.className().equals("")) {
                            //Every college has its own description
                            //TODO: look for a way to determine what type it is.
                            collegeType = span.text();
                        }
                    }
                }

            }
            //add the last unclosed college
            if (times != null) {
                College college = new College(collegeName, collegeType, times, location, teachers);
                currentDay.getColleges().add(college);
            }
        }

        //add the last unclosed college and day
        if (currentDay != null) {
            days.add(currentDay);
        }

        // returns the same object except for that the days are filled now.
        return new Week(week.getOwner(), week.getName(), week.getId(), days);
    }

    /**
     * This method parses html to get the empty weeks with name and id
     * @param doc The html page that should be parsed.
     * @return ArrayList<Week> all empty(only name and id) weeks that were found in the div.pagination
     */
    public static ArrayList<Week> parseWeekPager(Document doc) {
        //First select the div.pagination
        Elements pages = doc.select("div.pagination");
        //Elements list = pages.select("li");

        //Select all "a", does contain our data such as weekId and weekName
        Elements as = pages.select("a");

        ArrayList<Week> weeks = new ArrayList<>();

        for (Element a : as) {
            String href = a.attr("href");

            //Week href looks like: "/schedule/week:0/group:EIN2Va"
            String weekId = "0";
            Owner owner = null;
            if(href.contains("/group:")) {
                weekId = href.substring(href.indexOf("/week:") + 6, href.indexOf("/group:"));

                String ownerName = href.substring(href.indexOf("/group:") + 7);
                owner = new Group(ownerName);
            } else if(href.contains("/teacher:")) {
                weekId = href.substring(href.indexOf("/week:") + 6, href.indexOf("/teacher:"));
                String ownerName = href.substring(href.indexOf("/teacher:") + 9);
                owner = new Teacher(ownerName);
            }
            String weekName = a.text();

            Week week = new Week(owner, weekName, weekId);
            weeks.add(week);
        }
        return weeks;
    }

    public static ArrayList<Result> parseSearchResults(Document doc) {

        ArrayList<Result> results = new ArrayList<>();

        //First select all tables
        //Every table contains different kind of results like: groups or teachers
        Elements tables = doc.select("table");

        for(Element table : tables) {
            Elements tableRows = table.select("tr");
            for(Element row : tableRows) {
                Elements tableData = row.select("td");

                String name = null;
                String abbreviation = null;
                String type = null;
                for(Element data : tableData) {
                    Elements as = data.select("a");
                    for(int i = 0; i < as.size(); i++) {
                        Element a = as.get(i);

                        //first item is abbreviation
                        if(i == 0 && abbreviation == null && type == null) {
                            abbreviation = a.text();
                            String href = a.attr("href");
                            if(href.contains("group:")) {
                                type = S.GROUP;
                            } else if(href.contains("teacher:")) {
                                type = S.TEACHER;
                            } else if(href.contains("course:")) {
                                type = S.COURSE;
                            }
                        } else if(type != null) {
                            if(type.equals(S.GROUP)) {
                                //ok second line is the course name.
                                name = a.text();
                            } else if(type.equals(S.TEACHER) && i == 1) {
                                //teacher contains three lines. second line is fullname, third line is academy.
                                name = a.text();
                            } else if(type.equals(S.COURSE)) {
                                //no second line here, so name will stay empty.
                            }
                        }
                    }

                }
                //Jup we're done parsing this result, save it and move on.
                Result result = new Result(abbreviation, name, type);
                results.add(result);
                abbreviation = null;
                name = null;
                type = null;
            }
        }

        return results;
    }
}
