package soup.movie.core.preference.key;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public interface Key<ValueType> {
  enum Trait {
    SHAREABLE(true),
    INERASABLE(false);

    public static final int COUNT = values().length;

    public final boolean mutable;

    Trait(boolean mutable) {
      this.mutable = mutable;
    }
  }

  @NonNull
  String id();

  @NonNull
  ValueType defaultValue();

  @NonNull
  Trait trait();
}

abstract class BaseKey<ValueType> implements Key<ValueType> {
  protected static final String ID_FORMAT = "%s:%s";

  @NonNull
  private final String mId;

  @NonNull
  private final ValueType mDefaultValue;

  protected BaseKey(@NonNull String id, @NonNull ValueType defaultValue) {
    mId = makeUniqueId(getClass(), id);
    mDefaultValue = defaultValue;
  }

  @NonNull
  @VisibleForTesting
  static String makeUniqueId(@NonNull Class<?> concreteClass, @NonNull String id) {
    return String.format(ID_FORMAT, concreteClass.getName(), id);
  }

  @NonNull
  @Override
  public String id() {
    return mId;
  }

  @NonNull
  @Override
  public ValueType defaultValue() {
    return mDefaultValue;
  }
}
