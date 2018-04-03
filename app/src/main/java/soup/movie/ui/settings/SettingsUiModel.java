package soup.movie.ui.settings;

import android.support.annotation.NonNull;

import java.util.List;

import soup.movie.data.soup.model.TheaterCode;
import soup.movie.ui.BaseUiModel;

interface SettingsUiModel extends BaseUiModel {

    class InProgress implements SettingsUiModel {

        InProgress() {
        }

        @Override
        public String toString() {
            return "SettingsUiModel.InProgress{}";
        }
    }

    class Data implements SettingsUiModel {

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
