package soup.movie.data.soup.model;

import com.google.gson.annotations.JsonAdapter;

import soup.movie.data.soup.converter.TimeTableResponseDeserializer;

@JsonAdapter(TimeTableResponseDeserializer.class)
public class TimeTableResponse {

    private TimeTable timeTable;

    public TimeTableResponse() {
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    @Override
    public String toString() {
        return "TimeTableResponse{" +
                "timeTable=" + timeTable +
                '}';
    }
}
