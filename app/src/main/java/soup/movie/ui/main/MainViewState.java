package soup.movie.ui.main;

import soup.movie.ui.BaseViewState;

interface MainViewState extends BaseViewState {

    class NowState implements MainViewState {
        NowState() {
        }
    }

    class PlanState implements MainViewState {
        PlanState() {
        }
    }

    class SettingsState implements MainViewState {
        SettingsState() {
        }
    }
}
