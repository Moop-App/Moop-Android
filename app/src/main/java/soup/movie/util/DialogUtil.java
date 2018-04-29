package soup.movie.util;

import android.app.AlertDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import soup.movie.R;
import soup.movie.data.base.AsyncLoadListener;
import soup.movie.data.model.TheaterCode;
import soup.movie.util.function.Function;

public class DialogUtil {

    private DialogUtil() {}

    public static <T> void startDialogForResult(@NonNull Context context,
                                                @NonNull String title,
                                                @NonNull String confirmLabel,
                                                @NonNull List<T> items,
                                                @NonNull List<T> selectedItems,
                                                @NonNull Function<T, String> itemLabelFunction,
                                                @NonNull AsyncLoadListener<List<T>> asyncListener) {
        startDialogForResult(context, title, null, confirmLabel, null, items,
                selectedItems, itemLabelFunction, asyncListener);
    }

    public static <T> void startDialogForResult(
            @NonNull Context context,
            @NonNull String title,
            @Nullable String description,
            @NonNull String confirmLabel,
            @Nullable String cancelLabel,
            @NonNull List<T> items,
            @NonNull List<T> selectedItems,
            @NonNull Function<T, String> itemLabelFunction,
            @NonNull AsyncLoadListener<List<T>> asyncListener) {

        boolean[] selectionFlags = new boolean[items.size()];
        String[] itemLabels = ListUtil.toStringArray(items, itemLabelFunction);

        new AlertDialog.Builder(context)
                .setMultiChoiceItems(itemLabels, selectionFlags, (dialog, which, isChecked)
                        -> selectionFlags[which] = isChecked)
                .setPositiveButton("OK", (dialog, id) -> {
                    asyncListener.onLoaded(null);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss())
                .show();
    }

    public static void startDialogToSelectTheaters(
            @NonNull Context context, @NonNull AsyncLoadListener<List<TheaterCode>> asyncListener) {

        TheaterUtil.loadAsync(allItems -> {
            List<TheaterCode> selections = Collections.emptyList();//TheaterUtil.getMyTheaterList();
            HashMap<String, TheaterCode> currentSelectedItems = new HashMap<>();
            for (TheaterCode tc : selections) {
                currentSelectedItems.put(tc.getCode(), tc);
            }
            Set<String> codeSet = ListUtil.toStringSet(selections, TheaterCode::getCode);
            String[] itemLabels = ListUtil.toStringArray(allItems, TheaterCode::getName);
            final int size = allItems.size();
            boolean[] selectionFlags = new boolean[size];
            for (int i = 0; i < size; i++) {
                if (codeSet.contains(allItems.get(i).getCode())) {
                    selectionFlags[i] = true;
                }
            }

            new AlertDialog.Builder(context)
                    .setTitle(R.string.theater_select_title)
                    //.setMessage(R.string.theater_select_description)
                    .setMultiChoiceItems(itemLabels, selectionFlags,
                            (dialog, which, isChecked) -> {
                        TheaterCode theater = allItems.get(which);
                        if (isChecked) {
                            currentSelectedItems.put(theater.getCode(), theater);
                        } else {
                            currentSelectedItems.remove(theater.getCode());
                        }
                    })
                    .setPositiveButton(R.string.theater_select_action_confirm, (dialog, id) -> {
                        List<TheaterCode> tcList = new ArrayList<>(currentSelectedItems.values());
                        //TheaterUtil.saveMyTheaterList(tcList);
                        asyncListener.onLoaded(tcList);
                    })
                    .setNegativeButton(R.string.theater_select_action_cancel, (dialog, id) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }
}
