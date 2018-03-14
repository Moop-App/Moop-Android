package soup.movie.ui.filter;

import java.util.Arrays;

public class SwitchFilter<T> extends SingleChoiceFilter<T> {

    public SwitchFilter(T offValue, T onValue, boolean defaultOff) {
        super(Arrays.asList(offValue, onValue), defaultOff ? 0 : 1);
    }
}
