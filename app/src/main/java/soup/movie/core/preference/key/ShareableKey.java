package soup.movie.core.preference.key;

import android.support.annotation.NonNull;

public class ShareableKey<ValueType> extends BaseKey<ValueType> {
  public ShareableKey(@NonNull String id, @NonNull ValueType defaultValue) {
    super(id, defaultValue);
  }

  @NonNull
  @Override
  public Trait trait() {
    return Trait.SHAREABLE;
  }
}
