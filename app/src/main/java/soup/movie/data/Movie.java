package soup.movie.data;

import java.util.List;

public class Movie {

    private String movieCd; //영화코드
    private String movieNm; //영화명(국문)
    private String movieNmEn; //영화명(영문)
    private String prdtYear; //제작연도
    private String openDt; //개봉연도
    private String typeNm; //영화유형
    private String prdtStatNm; //제작상태
    private String nationAlt; //제작국가(전체)
    private String genreAlt; //영화장르(전체)
    private String repNationNm; //대표 제작국가명
    private String repGenreNm; //대표 장르명
    private List<Director> directors; //영화감독
    private List<Company> companys; //제작사

    public Movie() {
    }

    public String getMovieCd() {
        return movieCd;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public String getMovieNmEn() {
        return movieNmEn;
    }

    public String getPrdtYear() {
        return prdtYear;
    }

    public String getOpenDt() {
        return openDt;
    }

    public String getTypeNm() {
        return typeNm;
    }

    public String getPrdtStatNm() {
        return prdtStatNm;
    }

    public String getNationAlt() {
        return nationAlt;
    }

    public String getGenreAlt() {
        return genreAlt;
    }

    public String getRepNationNm() {
        return repNationNm;
    }

    public String getRepGenreNm() {
        return repGenreNm;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<Company> getCompanys() {
        return companys;
    }

    // for presenter

    public CharSequence getTitle() {
        return movieNm;
    }

    public CharSequence getSubtitle() {
        return genreAlt;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieCd='" + movieCd + '\'' +
                ", movieNm='" + movieNm + '\'' +
                ", movieNmEn='" + movieNmEn + '\'' +
                ", prdtYear='" + prdtYear + '\'' +
                ", openDt='" + openDt + '\'' +
                ", typeNm='" + typeNm + '\'' +
                ", prdtStatNm='" + prdtStatNm + '\'' +
                ", nationAlt='" + nationAlt + '\'' +
                ", genreAlt='" + genreAlt + '\'' +
                ", repNationNm='" + repNationNm + '\'' +
                ", repGenreNm='" + repGenreNm + '\'' +
                ", directors=" + directors +
                ", companys=" + companys +
                '}';
    }
}