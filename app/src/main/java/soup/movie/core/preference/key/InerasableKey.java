package soup.movie.core.preference.key;

import android.support.annotation.NonNull;

public class InerasableKey<ValueType> extends BaseKey<ValueType> {
  public InerasableKey(@NonNull String id, @NonNull ValueType defaultValue) {
    super(id, defaultValue);
  }

  @NonNull
  @Override
  public Trait trait() {
    return Trait.INERASABLE;
  }
}
