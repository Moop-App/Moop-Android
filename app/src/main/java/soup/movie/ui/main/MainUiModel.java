package soup.movie.ui.main;

import soup.movie.ui.BaseFragment;
import soup.movie.ui.BaseUiModel;
import soup.movie.ui.archive.ArchiveFragment;
import soup.movie.ui.boxoffice.BoxOfficeFragment;
import soup.movie.ui.home.HomeFragment;
import soup.movie.ui.settings.SettingsFragment;

interface MainUiModel extends BaseUiModel {

    BaseFragment newFragment();

    class Home implements MainUiModel {

        Home() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Home{}";
        }

        @Override
        public BaseFragment newFragment() {
            return HomeFragment.newInstance();
        }
    }

    class BoxOffice implements MainUiModel {

        BoxOffice() {
        }

        @Override
        public String toString() {
            return "MainUiModel.BoxOfficeMovie{}";
        }

        @Override
        public BaseFragment newFragment() {
            return BoxOfficeFragment.newInstance();
        }
    }

    class Archive implements MainUiModel {

        Archive() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Archive{}";
        }

        @Override
        public BaseFragment newFragment() {
            return ArchiveFragment.newInstance();
        }
    }

    class Settings implements MainUiModel {

        Settings() {
        }

        @Override
        public String toString() {
            return "MainUiModel.Settings{}";
        }

        @Override
        public BaseFragment newFragment() {
            return SettingsFragment.newInstance();
        }
    }
}
