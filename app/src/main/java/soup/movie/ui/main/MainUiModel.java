package soup.movie.ui.main;

import soup.movie.ui.BaseUiModel;

interface MainUiModel extends BaseUiModel {

    class Home implements MainUiModel {

        Home() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Home{}";
        }
    }

    class BoxOffice implements MainUiModel {

        BoxOffice() {
        }

        @Override
        public String toString() {
            return "MainUiModel.BoxOfficeMovie{}";
        }
    }

    class Archive implements MainUiModel {

        Archive() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Archive{}";
        }
    }

    class Settings implements MainUiModel {

        Settings() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Settings{}";
        }
    }
}
