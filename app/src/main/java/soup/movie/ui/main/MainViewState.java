package soup.movie.ui.main;

import soup.movie.ui.BaseViewState;

interface MainViewState extends BaseViewState {

    class NowState implements MainViewState {

        private final boolean isVerticalType;

        NowState(boolean verticalType) {
            isVerticalType = verticalType;
        }

        public boolean isVerticalType() {
            return isVerticalType;
        }

        @Override
        public String toString() {
            return "NowState{" +
                    "isVerticalType=" + isVerticalType +
                    '}';
        }
    }

    class PlanState implements MainViewState {

        private final boolean isVerticalType;

        PlanState(boolean verticalType) {
            isVerticalType = verticalType;
        }

        public boolean isVerticalType() {
            return isVerticalType;
        }

        @Override
        public String toString() {
            return "PlanState{" +
                    "isVerticalType=" + isVerticalType +
                    '}';
        }
    }

    class SettingsState implements MainViewState {
        SettingsState() {
        }

        @Override
        public String toString() {
            return "SettingsState{}";
        }
    }
}
