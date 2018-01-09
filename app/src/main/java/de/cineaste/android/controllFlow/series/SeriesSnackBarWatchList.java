package de.cineaste.android.controllFlow.series;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import de.cineaste.android.R;
import de.cineaste.android.adapter.series.SeriesListAdapter;
import de.cineaste.android.controllFlow.BaseSnackBar;
import de.cineaste.android.entity.series.Series;


public class SeriesSnackBarWatchList extends BaseSnackBar {

    private SeriesListAdapter adapter;

    SeriesSnackBarWatchList(LinearLayoutManager linearLayoutManager, View view, SeriesListAdapter adapter) {
        super(linearLayoutManager, view);
        this.adapter = adapter;
    }

    @Override
    public void getSnackBarLeftSwipe(final int position) {
        final Series seriesToBeDeleted = adapter.getItem(position);
        adapter.removeItem(position);

        final Snackbar mySnackbar = Snackbar.make(view,
                R.string.series_deleted, Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
        mySnackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        adapter.restoreDeletedItem(seriesToBeDeleted, position);
                        int first = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (first >= position) {
                            linearLayoutManager.scrollToPosition(position);
                        }
                        break;
                }
            }

        });
        mySnackbar.show();
    }

    //todo unmark all episodes as unwatched
    @Override
    public void getSnackBarRightSwipe(final int position, final int message) {
        final Series seriesToBeUpdated = adapter.getItem(position);
        adapter.toggleItemOnList(seriesToBeUpdated);

        final Snackbar mySnackbar = Snackbar.make(view,
                message, Snackbar.LENGTH_LONG);
        mySnackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
        mySnackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        adapter.restoreToggleItemOnList(seriesToBeUpdated, position);
                        int first = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (first >= position) {
                            linearLayoutManager.scrollToPosition(position);
                        }
                        break;
                }
            }

        });
        mySnackbar.show();
    }
}