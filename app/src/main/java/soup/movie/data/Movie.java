package soup.movie.data;

public class Movie {
    private final CharSequence mTitle;
    private final CharSequence mSubtitle;
    private final String mPosterUrl;

    public Movie(CharSequence title, String subtitle, String posterUrl) {
        mTitle = title;
        mSubtitle = subtitle;
        mPosterUrl = posterUrl;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public CharSequence getSubtitle() {
        return mSubtitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }
}
