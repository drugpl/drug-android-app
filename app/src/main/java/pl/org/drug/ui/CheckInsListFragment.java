package pl.org.drug.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import pl.org.drug.BootstrapServiceProvider;
import pl.org.drug.R;
import pl.org.drug.core.CheckIn;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.google.inject.Inject;
import pl.org.drug.core.DrugEvent;

import java.util.List;

public class CheckInsListFragment extends ItemListFragment<DrugEvent> {

    @Inject protected BootstrapServiceProvider serviceProvider;

    @Override
    protected void configureList(Activity activity, ListView listView) {
        super.configureList(activity, listView);

        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);

        getListAdapter()
                .addHeader(activity.getLayoutInflater()
                        .inflate(R.layout.checkins_list_item_labels, null));
    }

    @Override
    public void onDestroyView() {
        setListAdapter(null);

        super.onDestroyView();
    }

    @Override
    public Loader<List<DrugEvent>> onCreateLoader(int id, Bundle args) {
        final List<DrugEvent> initialItems = items;
        return new ThrowableLoader<List<DrugEvent>>(getActivity(), items) {

            @Override
            public List<DrugEvent> loadData() throws Exception {
                try {
                    return serviceProvider.getService().getCheckIns();
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
    protected SingleTypeAdapter<DrugEvent> createAdapter(List<DrugEvent> items) {
        return new CheckInsListAdapter(getActivity().getLayoutInflater(), items);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        CheckIn checkIn = ((CheckIn) l.getItemAtPosition(position));

        String uri = String.format("geo:%s,%s?q=%s",
                checkIn.getLocation().getLatitude(),
                checkIn.getLocation().getLongitude(),
                checkIn.getName());

        // Show a chooser that allows the user to decide how to display this data, in this case, map data.
        startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)), getString(R.string.choose)));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_checkins;
    }
}
