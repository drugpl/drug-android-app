package pl.org.drug.ui;

import static pl.org.drug.core.Constants.Extra.USER;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import pl.org.drug.BootstrapServiceProvider;
import pl.org.drug.R;
import pl.org.drug.core.AvatarLoader;
import pl.org.drug.core.DrugEvent;
import pl.org.drug.core.User;
import com.google.inject.Inject;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.util.Collections;
import java.util.List;

public class UserActivity extends RoboSherlockFragment implements LoaderManager.LoaderCallbacks<List<DrugEvent>> {

    @Inject private BootstrapServiceProvider serviceProvider;
    protected List<DrugEvent> items = Collections.emptyList();
    @InjectView(R.id.tv_title) protected TextView name;
    @InjectView(R.id.tv_description) protected TextView description;
    @InjectView(R.id.progressBar) protected ProgressBar progressBar;

    @Inject protected AvatarLoader avatarLoader;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //return inflater.inflate(R.layout.item_list, null);
      return inflater.inflate(R.layout.user_view, null);
    }

  @Override
  public Loader<List<DrugEvent>> onCreateLoader(int i, Bundle bundle) {
    final List<DrugEvent> initialItems = items;
    return new ThrowableLoader<List<DrugEvent>>(getActivity(), items) {
      @Override
      public List<DrugEvent> loadData() throws Exception {

        try {
          List<DrugEvent> latest = serviceProvider.getService().getCheckIns();
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

  protected Exception getException(final Loader<List<DrugEvent>> loader) {
    if (loader instanceof ThrowableLoader)
      return ((ThrowableLoader<List<DrugEvent>>) loader).clearException();
    else
      return null;
  }

  protected void showError(final int message) {
    Toaster.showLong(getActivity(), message);
  }

  protected int getErrorMessage(Exception exception) {
    return R.string.error_loading_drug;
  }

  @Override
  public void onLoadFinished(Loader<List<DrugEvent>> listLoader, List<DrugEvent> drugEvents) {
    Exception exception = getException(listLoader);
    if (exception != null) {
      showError(getErrorMessage(exception));
      return;
    }

    this.items = drugEvents;
    name.setText(items.get(items.size()-1).getTitle());
    description.setText(items.get(items.size()-1).getDescription());
    ViewUtils.setGone(progressBar, true);
    //getActivity().getLayoutInflater().inflate(R.layout.user_view, null);
  }

  @Override
  public void onLoaderReset(Loader<List<DrugEvent>> listLoader) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.user_view);
//
////        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
////        avatarLoader.bind(avatar, user);
////        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//
//    }


}
