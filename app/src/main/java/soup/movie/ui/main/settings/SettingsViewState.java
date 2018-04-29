package soup.movie.ui.main.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.model.TheaterCode;
import soup.movie.ui.BaseViewState;

interface SettingsViewState extends BaseViewState {

    class DoneState implements SettingsViewState {

        private List<TheaterCode> theaterList;

        DoneState(@NonNull List<TheaterCode> theaterList) {
            this.theaterList = theaterList;
        }

        public List<TheaterCode> getTheaterList() {
            return theaterList;
        }

        @Override
        public String toString() {
            return "DoneState{" +
                    "theaterList=" + theaterList +
                    '}';
        }
    }
}
