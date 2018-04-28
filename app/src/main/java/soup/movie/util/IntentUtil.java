package soup.movie.util;

import android.content.Intent;
import android.support.annotation.NonNull;

public class IntentUtil {

    private IntentUtil() {}

    public static Intent createShareIntentWithText(@NonNull String title, @NonNull String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, text)
                .setType("text/plain");
        return Intent.createChooser(shareIntent, title);
    }
}
