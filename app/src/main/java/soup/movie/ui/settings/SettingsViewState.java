package soup.movie.ui.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.TheaterCode;
import soup.movie.ui.BaseViewState;

interface SettingsViewState extends BaseViewState {

    class InProgress implements SettingsViewState {

        InProgress() {
        }

        @Override
        public String toString() {
            return "SettingsViewState.InProgress{}";
        }
    }

    class Data implements SettingsViewState {

        private List<TheaterCode> theaterList;

        Data(@NonNull List<TheaterCode> theaterList) {
            this.theaterList = theaterList;
        }

        public List<TheaterCode> getTheaterList() {
            return theaterList;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "theaterList=" + theaterList +
                    '}';
        }
    }
}
