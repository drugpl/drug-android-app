package pl.org.drug.ui;

import android.util.Log;
import android.view.LayoutInflater;

import pl.org.drug.R;
import pl.org.drug.core.CheckIn;
import pl.org.drug.core.DrugEvent;
import pl.org.drug.core.News;

import java.util.List;

import roboguice.util.Strings;

public class CheckInsListAdapter extends AlternatingColorListAdapter<DrugEvent> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public CheckInsListAdapter(LayoutInflater inflater, List<DrugEvent> items,
                           boolean selectable) {
        super(R.layout.checkin_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public CheckInsListAdapter(LayoutInflater inflater, List<DrugEvent> items) {
        super(R.layout.checkin_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_name, R.id.tv_date };
    }

    @Override
    protected void update(int position, DrugEvent item) {
        super.update(position, item);

        setText(0, item.getTitle());
    }
}
