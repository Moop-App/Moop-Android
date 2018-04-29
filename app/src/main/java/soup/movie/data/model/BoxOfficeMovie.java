package soup.movie.data.model;

import android.support.annotation.IntRange;

public class BoxOfficeMovie {

    @IntRange(from=1)
    private int rank;     // 순위 (1~)
    private int rankDiff; // 전일대비 순위 증감분
    private boolean isNewInRank; // 랭킹에 신규진입여부
    private String id; // 영화의 대표코드
    private String name; // 영화명(국문)
    private String openDate; // 영화의 개봉일
    private int audienceCount; // 해당일의 관객수
    private int audienceAcc; // 누적관객수

    public BoxOfficeMovie(int rank, int rankDiff, boolean isNewInRank, String id, String name,
                          String openDate, int audienceCount, int audienceAcc) {
        this.rank = rank;
        this.rankDiff = rankDiff;
        this.isNewInRank = isNewInRank;
        this.id = id;
        this.name = name;
        this.openDate = openDate;
        this.audienceCount = audienceCount;
        this.audienceAcc = audienceAcc;
    }

    public int getRank() {
        return rank;
    }

    public int getRankDiff() {
        return rankDiff;
    }

    public boolean isIsNewInRank() {
        return isNewInRank;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOpenDate() {
        return openDate;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public int getAudienceAcc() {
        return audienceAcc;
    }

    @Override
    public String toString() {
        return "BoxOfficeMovie{" +
                "rank=" + rank +
                ", rankDiff=" + rankDiff +
                ", isNewInRank=" + isNewInRank +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", openDate='" + openDate + '\'' +
                ", audienceCount=" + audienceCount +
                ", audienceAcc=" + audienceAcc +
                '}';
    }
}
