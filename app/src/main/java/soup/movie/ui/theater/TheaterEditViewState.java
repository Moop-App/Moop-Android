package soup.movie.ui.theater;

import java.util.List;

import soup.movie.data.model.TheaterCode;
import soup.movie.ui.BaseViewState;

class TheaterEditViewState implements BaseViewState {

    private final List<TheaterCode> allTheaters;
    private final List<TheaterCode> selectedTheaters;

    public TheaterEditViewState(List<TheaterCode> allTheaters, List<TheaterCode> selectedTheaters) {
        this.allTheaters = allTheaters;
        this.selectedTheaters = selectedTheaters;
    }

    public List<TheaterCode> getAllTheaters() {
        return allTheaters;
    }

    public List<TheaterCode> getSelectedTheaters() {
        return selectedTheaters;
    }

    @Override
    public String toString() {
        return "TheaterEditViewState{" +
                "allTheaters=" + allTheaters +
                ", selectedTheaters=" + selectedTheaters +
                '}';
    }
}
