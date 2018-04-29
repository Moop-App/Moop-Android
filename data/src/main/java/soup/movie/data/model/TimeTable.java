package soup.movie.data.model;

import java.util.List;

public class TimeTable {

    private List<Day> dayList;

    public TimeTable(List<Day> dayList) {
        this.dayList = dayList;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "dayList=" + dayList +
                '}';
    }
}
