package soup.movie.data.model;

import java.util.List;

import soup.movie.data.MovieConstants;

public class BoxOfficeMovieList {

    @MovieConstants.Source
    private int mSource; // 데이터 소스

    @MovieConstants.BoxOfficeType
    private int mBoxOfficeType;
    private String mStartDate;
    private String mEndDate;
    private List<BoxOfficeMovie> mBoxOfficeMovieList;

    public BoxOfficeMovieList(int source, int boxOfficeType, String startDate, String endDate,
                              List<BoxOfficeMovie> boxOfficeMovieList) {
        this.mSource = source;
        this.mBoxOfficeType = boxOfficeType;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mBoxOfficeMovieList = boxOfficeMovieList;
    }

    public int getSource() {
        return mSource;
    }

    public int getBoxOfficeType() {
        return mBoxOfficeType;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public List<BoxOfficeMovie> getBoxOfficeMovieList() {
        return mBoxOfficeMovieList;
    }

    @Override
    public String toString() {
        return "BoxOfficeMovieList{" +
                "mSource=" + mSource +
                ", mBoxOfficeType=" + mBoxOfficeType +
                ", mStartDate='" + mStartDate + '\'' +
                ", mEndDate='" + mEndDate + '\'' +
                ", mBoxOfficeMovieList=" + mBoxOfficeMovieList +
                '}';
    }
}
