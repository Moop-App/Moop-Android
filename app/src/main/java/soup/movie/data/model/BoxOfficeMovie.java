package soup.movie.data.model;

import android.support.annotation.IntRange;

public class BoxOfficeMovie {

    @IntRange(from=1)
    private int mRank;     // 순위 (1~)
    private int mRankDiff; // 전일대비 순위 증감분
    private boolean mIsNewInRank; // 랭킹에 신규진입여부
    private String mId; // 영화의 대표코드
    private String mName; // 영화명(국문)
    private String mOpenDate; // 영화의 개봉일
    private int mAudienceCount; // 해당일의 관객수
    private int mAudienceAcc; // 누적관객수

    public BoxOfficeMovie(int rank, int rankDiff, boolean isNewInRank, String id, String name,
                          String openDate, int audienceCount, int audienceAcc) {
        this.mRank = rank;
        this.mRankDiff = rankDiff;
        this.mIsNewInRank = isNewInRank;
        this.mId = id;
        this.mName = name;
        this.mOpenDate = openDate;
        this.mAudienceCount = audienceCount;
        this.mAudienceAcc = audienceAcc;
    }

    public int getRank() {
        return mRank;
    }

    public int getRankDiff() {
        return mRankDiff;
    }

    public boolean isIsNewInRank() {
        return mIsNewInRank;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getOpenDate() {
        return mOpenDate;
    }

    public int getAudienceCount() {
        return mAudienceCount;
    }

    public int getAudienceAcc() {
        return mAudienceAcc;
    }

    @Override
    public String toString() {
        return "BoxOfficeMovie{" +
                "mRank=" + mRank +
                ", mRankDiff=" + mRankDiff +
                ", mIsNewInRank=" + mIsNewInRank +
                ", mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mOpenDate='" + mOpenDate + '\'' +
                ", mAudienceCount=" + mAudienceCount +
                ", mAudienceAcc=" + mAudienceAcc +
                '}';
    }
}
