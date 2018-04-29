package soup.movie.data.model;

import java.util.List;

import soup.movie.data.MovieConstants;

public class BoxOfficeMovieList {

    @MovieConstants.Source
    private int source; // 데이터 소스

    @MovieConstants.BoxOfficeType
    private int boxOfficeType;
    private String startDate;
    private String endDate;
    private List<BoxOfficeMovie> boxOfficeMovieList;

    public BoxOfficeMovieList(int source, int boxOfficeType, String startDate, String endDate,
                              List<BoxOfficeMovie> boxOfficeMovieList) {
        this.source = source;
        this.boxOfficeType = boxOfficeType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.boxOfficeMovieList = boxOfficeMovieList;
    }

    public int getSource() {
        return source;
    }

    public int getBoxOfficeType() {
        return boxOfficeType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public List<BoxOfficeMovie> getBoxOfficeMovieList() {
        return boxOfficeMovieList;
    }

    @Override
    public String toString() {
        return "BoxOfficeMovieList{" +
                "source=" + source +
                ", boxOfficeType=" + boxOfficeType +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", boxOfficeMovieList=" + boxOfficeMovieList +
                '}';
    }
}
