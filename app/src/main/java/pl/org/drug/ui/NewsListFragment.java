package pl.org.drug.ui;

import static pl.org.drug.core.Constants.Extra.NEWS_ITEM;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import pl.org.drug.BootstrapServiceProvider;
import pl.org.drug.R;
import pl.org.drug.core.News;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.google.inject.Inject;
import pl.org.drug.core.Presentation;

import java.util.List;

public class NewsListFragment extends ItemListFragment<Presentation> {

    @Inject protected BootstrapServiceProvider serviceProvider;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_presentations);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.news_list_item_labels, null));
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<Presentation>> onCreateLoader(int id, Bundle args) {
        final List<Presentation> initialItems = items;
        return new ThrowableLoader<List<Presentation>>(getActivity(), items) {

            @Override
            public List<Presentation> loadData() throws Exception {
                try {
                    return serviceProvider.getService().getNews();
                } catch (OperationCanceledException e) {
                    Activity activity = getActivity();
                    if (activity != null)
                      activity.finish();
                    return initialItems;
                }
            }
        };
    }

    @Override
    protected SingleTypeAdapter<Presentation> createAdapter(List<Presentation> items) {
        return new NewsListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Presentation news = ((Presentation) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), NewsActivity.class).putExtra(NEWS_ITEM, news));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }
}
