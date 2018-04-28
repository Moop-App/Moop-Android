package soup.movie.ui.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.TheaterCode;
import soup.movie.ui.BaseViewState;

interface SettingsViewState extends BaseViewState {

    class LoadingState implements SettingsViewState {

        LoadingState() {
        }

        @Override
        public String toString() {
            return "SettingsViewState.LoadingState{}";
        }
    }

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
