package soup.movie.data;

import com.google.gson.annotations.SerializedName;

public class BoxOfficeMovie {

    @SerializedName("rnum")
    private String rnum;

    @SerializedName("rank")
    private String rank;

    @SerializedName("rankInten")
    private String rankInten;

    @SerializedName("rankOldAndNew")
    private String rankOldAndNew;

    @SerializedName("movieCd")
    private String movieCd;

    @SerializedName("movieNm")
    private String movieNm;

    @SerializedName("openDt")
    private String openDt;

    @SerializedName("salesAmt")
    private String salesAmt;

    @SerializedName("salesShare")
    private String salesShare;

    @SerializedName("salesInten")
    private String salesInten;

    @SerializedName("salesChange")
    private String salesChange;

    @SerializedName("salesAcc")
    private String salesAcc;

    @SerializedName("audiCnt")
    private String audiCnt;

    @SerializedName("audiInten")
    private String audiInten;

    @SerializedName("audiChange")
    private String audiChange;

    @SerializedName("audiAcc")
    private String audiAcc;

    @SerializedName("scrnCnt")
    private String scrnCnt;

    @SerializedName("showCnt")
    private String showCnt;

    // 순번을 출력합니다.
    public String getRnum() {
        return rnum;
    }

    // 해당일자의 박스오피스 순위를 출력합니다.
    public String getRank() {
        return rank;
    }

    public String getRankInten() {
        return rankInten;
    }

    public String getRankOldAndNew() {
        return rankOldAndNew;
    }

    public String getMovieCd() {
        return movieCd;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public String getOpenDt() {
        return openDt;
    }

    public String getSalesAmt() {
        return salesAmt;
    }

    public String getSalesShare() {
        return salesShare;
    }

    public String getSalesInten() {
        return salesInten;
    }

    public String getSalesChange() {
        return salesChange;
    }

    public String getSalesAcc() {
        return salesAcc;
    }

    public String getAudiCnt() {
        return audiCnt;
    }

    public String getAudiInten() {
        return audiInten;
    }

    public String getAudiChange() {
        return audiChange;
    }

    public String getAudiAcc() {
        return audiAcc;
    }

    public String getScrnCnt() {
        return scrnCnt;
    }

    public String getShowCnt() {
        return showCnt;
    }

    @Override
    public String toString() {
        return "BoxOfficeMovie{" +
                "rnum='" + rnum + '\'' +
                ", rank='" + rank + '\'' +
                ", rankInten='" + rankInten + '\'' +
                ", rankOldAndNew='" + rankOldAndNew + '\'' +
                ", movieCd='" + movieCd + '\'' +
                ", movieNm='" + movieNm + '\'' +
                ", openDt='" + openDt + '\'' +
                ", salesAmt='" + salesAmt + '\'' +
                ", salesShare='" + salesShare + '\'' +
                ", salesInten='" + salesInten + '\'' +
                ", salesChange='" + salesChange + '\'' +
                ", salesAcc='" + salesAcc + '\'' +
                ", audiCnt='" + audiCnt + '\'' +
                ", audiInten='" + audiInten + '\'' +
                ", audiChange='" + audiChange + '\'' +
                ", audiAcc='" + audiAcc + '\'' +
                ", scrnCnt='" + scrnCnt + '\'' +
                ", showCnt='" + showCnt + '\'' +
                '}';
    }
}
