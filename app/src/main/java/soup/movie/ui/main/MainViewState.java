package soup.movie.ui.main;

import soup.movie.ui.BaseFragment;
import soup.movie.ui.BaseViewState;
import soup.movie.ui.home.HomeFragment;
import soup.movie.ui.settings.SettingsFragment;

interface MainViewState extends BaseViewState {

    BaseFragment newFragment();

    class Home implements MainViewState {

        Home() {
        }

        @Override
        public String toString() {
            return "MainViewState.Home{}";
        }

        @Override
        public BaseFragment newFragment() {
            return HomeFragment.newInstance();
        }
    }

    class Settings implements MainViewState {

        Settings() {
        }

        @Override
        public String toString() {
            return "MainViewState.Settings{}";
        }

        @Override
        public BaseFragment newFragment() {
            return SettingsFragment.newInstance();
        }
    }
}
