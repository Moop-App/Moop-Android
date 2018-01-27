package soup.movie.data;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.source.remote.service.KobisApiService;

public class MovieListRequest implements QueryMapProvider {

    private String key; //발급받은키 값을 입력합니다.
    private String curPage; //현재 페이지를 지정합니다.(default : “1”)
    private String itemPerPage; //결과 ROW 의 개수를 지정합니다.(default : “10”)
    private String movieNm; //영화명으로 조회합니다. (UTF-8 인코딩)
    private String directorNm; //감독명으로 조회합니다. (UTF-8 인코딩)
    private String openStartDt; //YYYY형식의 조회시작 개봉연도를 입력합니다.
    private String openEndDt; //YYYY형식의 조회종료 개봉연도를 입력합니다.
    private String prdtStartYear; //YYYY형식의 조회시작 제작연도를 입력합니다.
    private String prdtEndYear; //YYYY형식의 조회종료 제작연도를 입력합니다.
    private String repNationCd; //N개의 국적으로 조회할 수 있으며, 국적코드는 공통코드 조회 서비스에서 “2204” 로서 조회된 국적코드입니다. (default : 전체)
    private String movieTypeCd; //N개의 영화유형코드로 조회할 수 있으며, 영화유형코드는 공통코드 조회 서비스에서 “2201”로서 조회된 영화유형코드입니다. (default: 전체)

    public MovieListRequest() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public void setItemPerPage(String itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }

    public void setDirectorNm(String directorNm) {
        this.directorNm = directorNm;
    }

    public void setOpenStartDt(String openStartDt) {
        this.openStartDt = openStartDt;
    }

    public void setOpenEndDt(String openEndDt) {
        this.openEndDt = openEndDt;
    }

    public void setPrdtStartYear(String prdtStartYear) {
        this.prdtStartYear = prdtStartYear;
    }

    public void setPrdtEndYear(String prdtEndYear) {
        this.prdtEndYear = prdtEndYear;
    }

    public void setRepNationCd(String repNationCd) {
        this.repNationCd = repNationCd;
    }

    public void setMovieTypeCd(String movieTypeCd) {
        this.movieTypeCd = movieTypeCd;
    }

    @Override
    public Map<String, String> toQueryMap() {
        // dummy
        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("key", KobisApiService.API_KEY);
        queryMap.put("openStartDt", "2018");
        queryMap.put("openEndDt", "2018");
        return queryMap;
    }
}