package soup.movie.data.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MovieAppUtil {

    private MovieAppUtil() {}

    private static final String PACKAGE_NAME_CGV = "com.cgv.android.movieapp";

    public static void executeCgvApp(@NonNull Context ctx) {
        final String pkgName = PACKAGE_NAME_CGV;
        if (!executeApp(ctx, pkgName)) {
            executePlayStoreForApp(ctx, pkgName);
        }
    }

    private static boolean executeApp(@NonNull Context ctx, @NonNull String pkgName) {
        Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (launchIntent != null) {
            ctx.startActivity(launchIntent);
            return true;
        }
        return false;
    }

    private static void executePlayStoreForApp(@NonNull Context ctx, @NonNull String pkgName) {
        try {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + pkgName)));
        } catch (ActivityNotFoundException e) {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + pkgName)));
        }
    }

    private static void executeWebPage(@NonNull Context ctx, @NonNull String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(webIntent);
    }

}
