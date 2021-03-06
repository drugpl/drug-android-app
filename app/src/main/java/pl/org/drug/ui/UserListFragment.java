package pl.org.drug.ui;

import static pl.org.drug.core.Constants.Extra.NEWS_ITEM;
import static pl.org.drug.core.Constants.Extra.USER;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import pl.org.drug.BootstrapServiceProvider;
import pl.org.drug.R;
import pl.org.drug.core.AvatarLoader;
import pl.org.drug.core.News;
import pl.org.drug.core.User;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.google.inject.Inject;

import java.util.Collections;
import java.util.List;

public class UserListFragment  extends ItemListFragment<User> {

    @Inject private BootstrapServiceProvider serviceProvider;
    @Inject private AvatarLoader avatars;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(R.string.no_drugs);
    }

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter().addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.user_list_item_labels, null));
    }



    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        final List<User> initialItems = items;
        return new ThrowableLoader<List<User>>(getActivity(), items) {
            @Override
            public List<User> loadData() throws Exception {

                try {
                    List<User> latest = serviceProvider.getService().getUsers();
                    if (latest != null)
                        return latest;
                    else
                        return Collections.emptyList();
                } catch (OperationCanceledException e) {
                    Activity activity = getActivity();
                    if (activity != null)
                        activity.finish();
                    return initialItems;
                }
            }
        };

    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        User user = ((User) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), UserActivity.class).putExtra(USER, user));
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> items) {
        super.onLoadFinished(loader, items);

    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_drug;
    }

    @Override
    protected SingleTypeAdapter<User> createAdapter(List<User> items) {
        return new UserListAdapter(getActivity().getLayoutInflater(), items, avatars);
    }
}
