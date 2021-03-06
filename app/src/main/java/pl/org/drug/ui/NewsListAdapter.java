package pl.org.drug.ui;

import android.view.LayoutInflater;

import pl.org.drug.R;
import pl.org.drug.core.News;
import pl.org.drug.core.Presentation;
import pl.org.drug.ui.AlternatingColorListAdapter;

import java.util.List;

public class NewsListAdapter extends AlternatingColorListAdapter<Presentation> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public NewsListAdapter(LayoutInflater inflater, List<Presentation> items,
                               boolean selectable) {
        super(R.layout.news_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public NewsListAdapter(LayoutInflater inflater, List<Presentation> items) {
        super(R.layout.news_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_title, R.id.tv_summary,
                R.id.tv_date };
    }

    @Override
    protected void update(int position, Presentation item) {
        super.update(position, item);

        setText(0, item.getTitle());
        setText(1, item.getSpeaker());
        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
