package soup.movie.ui.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import soup.movie.R;
import soup.movie.data.Movie;

public class MoviePreviewFragment extends Fragment implements MoviePreviewContract.View {

    private MoviePreviewContract.Presenter mPresenter;

    private MoviePreviewContract.AdapterView mAdapterView;

    public MoviePreviewFragment() {
        mPresenter = new MoviePreviewPresenter(this);
    }

    public static MoviePreviewFragment newInstance() {
        return new MoviePreviewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_preview_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            MoviePreviewListAdapter adapterView = new MoviePreviewListAdapter();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapterView);
            mAdapterView = adapterView;
            mPresenter.bind();
            mPresenter.loadItems();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.unbind();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showList(List<Movie> items) {
        MoviePreviewContract.AdapterView adapterView = mAdapterView;
        if (adapterView != null) {
            adapterView.updateList(items);
        }
    }
}
