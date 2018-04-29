package soup.movie.ui.main.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.model.TheaterCode;
import soup.movie.ui.BaseViewState;

interface SettingsViewState extends BaseViewState {

    class DoneState implements SettingsViewState {

        private boolean isHomeTypeVertical;
        private List<TheaterCode> theaterList;

        DoneState(boolean isHomeTypeVertical, @NonNull List<TheaterCode> theaterList) {
            this.isHomeTypeVertical = isHomeTypeVertical;
            this.theaterList = theaterList;
        }

        public boolean isHomeTypeVertical() {
            return isHomeTypeVertical;
        }

        public List<TheaterCode> getTheaterList() {
            return theaterList;
        }

        @Override
        public String toString() {
            return "DoneState{" +
                    "isHomeTypeVertical=" + isHomeTypeVertical +
                    ", theaterList=" + theaterList +
                    '}';
        }
    }
}
