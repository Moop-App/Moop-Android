package soup.movie.core.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import soup.movie.MovieApplication;
import soup.movie.core.preference.key.Key;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Preference {
  private interface Singleton {
    Preference INSTANCE = new Preference();
  }

  public static Preference getInstance() {
    return Singleton.INSTANCE;
  }

  @NonNull
  private final List<SharedPreferences> mStoragePool;

  private Preference() {
    mStoragePool = createStoragePool();
  }

  @NonNull
  private static List<SharedPreferences> createStoragePool() {
    List<SharedPreferences> storagePool = new ArrayList<>(Key.Trait.COUNT);
    for (Key.Trait trait : Key.Trait.values()) {
      switch (trait) {
        case SHAREABLE:
          storagePool.add(trait.ordinal(), createShareableStorage());
          break;
        case INERASABLE:
          storagePool.add(trait.ordinal(), createInerasableStorage());
          break;
        default:
          throw new UnsupportedOperationException(trait + " not handled in switch");
      }
    }
    return storagePool;
  }

  @VisibleForTesting
  List<SharedPreferences> storagePool() {
    return mStoragePool;
  }

  @NonNull
  private static SharedPreferences createShareableStorage() {
    return PreferenceManager.getDefaultSharedPreferences(MovieApplication.getInstance());
  }

  @NonNull
  private static SharedPreferences createInerasableStorage() {
    return MovieApplication.getInstance().getSharedPreferences(Key.Trait.INERASABLE.name(),
                                                              Context.MODE_PRIVATE);
  }

  @NonNull
  public boolean getBoolean(@NonNull Key<Boolean> key) {
    return getStorage(key.trait()).getBoolean(key.id(), key.defaultValue());
  }

  @NonNull
  public double getDouble(@NonNull Key<Double> key) {
    return NumberUtils.toDouble(getStringInternal(key.trait(), key.id(), ""), key.defaultValue());
  }

  @NonNull
  public float getFloat(@NonNull Key<Float> key) {
    return getStorage(key.trait()).getFloat(key.id(), key.defaultValue());
  }

  @NonNull
  public int getInt(@NonNull Key<Integer> key) {
    return getStorage(key.trait()).getInt(key.id(), key.defaultValue());
  }

  @NonNull
  public long getLong(@NonNull Key<Long> key) {
    return getStorage(key.trait()).getLong(key.id(), key.defaultValue());
  }

  @NonNull
  public String getString(@NonNull Key<String> key) {
    return getStorage(key.trait()).getString(key.id(), key.defaultValue());
  }

  @NonNull
  public Set<String> getStringSet(@NonNull Key<Set<String>> key) {
    return getStorage(key.trait()).getStringSet(key.id(), key.defaultValue());
  }

  public void putBoolean(@NonNull Key<Boolean> key, boolean value) {
    persistWith(getMutableStorage(key.trait()).putBoolean(key.id(), value));
  }

  public void putDouble(@NonNull Key<Double> key, double value) {
    putStringInternal(key.trait(), key.id(), Double.toString(value));
  }

  public void putFloat(@NonNull Key<Float> key, float value) {
    persistWith(getMutableStorage(key.trait()).putFloat(key.id(), value));
  }

  public void putInt(@NonNull Key<Integer> key, int value) {
    persistWith(getMutableStorage(key.trait()).putInt(key.id(), value));
  }

  public void putLong(@NonNull Key<Long> key, long value) {
    persistWith(getMutableStorage(key.trait()).putLong(key.id(), value));
  }

  public void putString(@NonNull Key<String> key, String value) {
    putStringInternal(key.trait(), key.id(), value);
  }

  public void putStringSet(@NonNull Key<Set<String>> key, Set<String> value) {
    persistWith(getMutableStorage(key.trait()).putStringSet(key.id(), value));
  }

  @NonNull
  private String getStringInternal(@NonNull Key.Trait trait,
                                   @NonNull String key,
                                   @NonNull String defaultValue) {
    return getStorage(trait).getString(key, defaultValue);
  }

  @NonNull
  private void putStringInternal(@NonNull Key.Trait trait,
                                 @NonNull String key,
                                 @NonNull String value) {
    persistWith(getMutableStorage(trait).putString(key, value));
  }

  @NonNull
  @VisibleForTesting
  Object get(@NonNull Key key) {
    final Object defaultValue = key.defaultValue();
    if (defaultValue instanceof Boolean)
      return getBoolean(key);
    if (defaultValue instanceof Double)
      return getDouble(key);
    if (defaultValue instanceof Float)
      return getFloat(key);
    if (defaultValue instanceof Integer)
      return getInt(key);
    if (defaultValue instanceof Long)
      return getLong(key);
    if (defaultValue instanceof String)
      return getString(key);
    if (defaultValue instanceof Set)
      return getStringSet(key);
    throw new UnsupportedOperationException(defaultValue.getClass() + " is unsupported");
  }

  @NonNull
  @VisibleForTesting
  void put(@NonNull Key key, @NonNull Object value) {
    if (value instanceof Boolean)
      putBoolean(key, (boolean)value);
    else if (value instanceof Double)
      putDouble(key, (double)value);
    else if (value instanceof Float)
      putFloat(key, (float)value);
    else if (value instanceof Integer)
      putInt(key, (int)value);
    else if (value instanceof Long)
      putLong(key, (long)value);
    else if (value instanceof String)
      putString(key, (String)value);
    else if (value instanceof Set)
      putStringSet(key, (Set<String>)value);
    else
      throw new UnsupportedOperationException(value.getClass() + " is unsupported");
  }

  public void clear() {
    for (Key.Trait trait : Key.Trait.values()) {
      if (trait.mutable)
        persistWith(getMutableStorage(trait).clear());
    }
  }

  public void reset() {
    for (Key.Trait trait : Key.Trait.values()) {
      persistWith(getMutableStorage(trait).clear());
    }
  }

  public void remove(@NonNull Key key) {
    final Key.Trait trait = key.trait();
    if (!trait.mutable)
      throw new UnsupportedOperationException(trait + " is not mutable");
    persistWith(getMutableStorage(trait).remove(key.id()));
  }

  @NonNull
  @VisibleForTesting
  SharedPreferences getStorage(Key.Trait trait) {
    return mStoragePool.get(trait.ordinal());
  }

  @NonNull
  @VisibleForTesting
  SharedPreferences.Editor getMutableStorage(@NonNull Key.Trait trait) {
    return getStorage(trait).edit();
  }

  private void persistWith(@NonNull SharedPreferences.Editor editor) {
      editor.apply();
  }
}
