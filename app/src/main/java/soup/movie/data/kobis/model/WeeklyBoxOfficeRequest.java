package soup.movie.data.kobis.model;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import soup.movie.data.core.QueryMapProvider;
import soup.movie.data.kobis.service.KobisApiService;

public class WeeklyBoxOfficeRequest implements QueryMapProvider {

    private String key;          //
    private String targetDt;     // 조회하고자 하는 날짜를 yyyymmdd 형식으로 입력합니다.
    private String weekGb;       // “0” : 주간 (월~일), “1” : 주말 (금~일) (default), “2” : 주중 (월~목)
    private String itemPerPage;  // 결과 ROW 의 개수를 지정합니다.(default : “10”, 최대 : “10“)
    private String multiMovieYn; // “Y” : 다양성 영화 “N” : 상업영화 (default : 전체)
    private String repNationCd;  // “K: : 한국영화 “F” : 외국영화 (default : 전체)
    private String wideAreaCd;   // 상영지역별로 조회할 수 있으며, 지역코드는 공통코드 조회 서비스에서 “0105000000” 로서 조회된 지역코드입니다. (default : 전체)

    public WeeklyBoxOfficeRequest() {
    }

    public WeeklyBoxOfficeRequest setKey(@NonNull String key) {
        this.key = key;
        return this;
    }

    public WeeklyBoxOfficeRequest setTargetDt(@NonNull String targetDt) {
        this.targetDt = targetDt;
        return this;
    }

    public WeeklyBoxOfficeRequest setWeekGb(String weekGb) {
        this.weekGb = weekGb;
        return this;
    }

    public WeeklyBoxOfficeRequest setItemPerPage(String itemPerPage) {
        this.itemPerPage = itemPerPage;
        return this;
    }

    public WeeklyBoxOfficeRequest setMultiMovieYn(String multiMovieYn) {
        this.multiMovieYn = multiMovieYn;
        return this;
    }

    public WeeklyBoxOfficeRequest setWideAreaCd(String wideAreaCd) {
        this.wideAreaCd = wideAreaCd;
        return this;
    }

    public WeeklyBoxOfficeRequest setRepNationCd(String repNationCd) {
        this.repNationCd = repNationCd;
        return this;
    }

    @Override
    public Map<String, String> toQueryMap() {
        // dummy
        HashMap<String, String> queryMap = new HashMap<String, String>();
        queryMap.put("key", KobisApiService.API_KEY);
        queryMap.put("targetDt", "20180310");
        return queryMap;
    }
}