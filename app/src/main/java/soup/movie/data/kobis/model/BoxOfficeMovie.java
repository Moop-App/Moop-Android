package soup.movie.data.kobis.model;

import com.google.gson.annotations.SerializedName;

public class BoxOfficeMovie {

    @SerializedName("rnum")
    private String rnum; // 순번

    @SerializedName("rank")
    private String rank; // 해당일자의 박스오피스 순위

    @SerializedName("rankInten")
    private String rankInten; // 전일대비 순위의 증감분

    @SerializedName("rankOldAndNew")
    private String rankOldAndNew; // 랭킹에 신규진입여부, “OLD” : 기존 , “NEW” : 신규

    @SerializedName("movieCd")
    private String movieCd; // 영화의 대표코드

    @SerializedName("movieNm")
    private String movieNm; // 영화명(국문)

    @SerializedName("openDt")
    private String openDt; // 영화의 개봉일

    @SerializedName("salesAmt")
    private String salesAmt; // 해당일의 매출액

    @SerializedName("salesShare")
    private String salesShare; // 해당일자 상영작의 매출총액 대비 해당 영화의 매출비율

    @SerializedName("salesInten")
    private String salesInten; // 전일 대비 매출액 증감분

    @SerializedName("salesChange")
    private String salesChange; // 전일 대비 매출액 증감 비율

    @SerializedName("salesAcc")
    private String salesAcc; // 누적매출액

    @SerializedName("audiCnt")
    private String audiCnt; // 해당일의 관객수

    @SerializedName("audiInten")
    private String audiInten; // 전일 대비 관객수 증감분

    @SerializedName("audiChange")
    private String audiChange; // 전일 대비 관객수 증감 비율

    @SerializedName("audiAcc")
    private String audiAcc; // 누적관객수

    @SerializedName("scrnCnt")
    private String scrnCnt; // 해당일자에 상영한 스크린수

    @SerializedName("showCnt")
    private String showCnt; // 해당일자에 상영된 횟수

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
