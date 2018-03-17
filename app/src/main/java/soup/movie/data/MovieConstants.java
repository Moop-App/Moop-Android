package soup.movie.data;

import android.support.annotation.IntDef;

public class MovieConstants {

    private MovieConstants() {
    }

    @IntDef({
            Source.PRIVATE,
            Source.KOBIS, Source.KOREA_FILM, Source.MOVIST,
            Source.TMDB, Source.OMDB,
            Source.NAVER, Source.DAUM,
            Source.CGV, Source.LOTTE_CINEMA, Source.MEGABOX
    })
    public @interface Source {

        int PRIVATE      = 1;

        // 영화 관련 API
        int KOBIS        = 11;
        int KOREA_FILM   = 12;
        int MOVIST       = 13;
        int TMDB         = 14; // 해외
        int OMDB         = 15; // 해외

        // 인터넷 포털
        int NAVER        = 21;
        int DAUM         = 22;

        // 영화관
        int CGV          = 31;
        int LOTTE_CINEMA = 32;
        int MEGABOX      = 33;
    }

    @IntDef({
            BoxOfficeType.DAY,
            BoxOfficeType.WEEK, BoxOfficeType.WEEKDAYS, BoxOfficeType.WEEKEND
    })
    public @interface BoxOfficeType {
        int DAY      = 1;
        int WEEK     = 2;
        int WEEKDAYS = 3;
        int WEEKEND  = 4;
    }
}
