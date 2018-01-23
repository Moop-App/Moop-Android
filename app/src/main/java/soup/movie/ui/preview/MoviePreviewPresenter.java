package soup.movie.ui.preview;

import java.util.Arrays;

import soup.movie.DummyContents;

public class MoviePreviewPresenter implements MoviePreviewContract.Presenter {

    private MoviePreviewContract.View mView;

    MoviePreviewPresenter(MoviePreviewContract.View view) {
        mView = view;
    }

    @Override
    public void bind() {
    }

    @Override
    public void unbind() {
    }

    @Override
    public void loadItems() {
        mView.showList(Arrays.asList(DummyContents.ITEMS));
    }
}
