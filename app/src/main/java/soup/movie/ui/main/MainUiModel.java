package soup.movie.ui.main;

import soup.movie.ui.BaseFragment;
import soup.movie.ui.BaseUiModel;
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
