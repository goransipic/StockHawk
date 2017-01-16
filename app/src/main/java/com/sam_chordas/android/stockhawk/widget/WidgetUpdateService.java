package com.sam_chordas.android.stockhawk.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.DetailStocksActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by User on 15.1.2017..
 */

public class WidgetUpdateService extends IntentService {
    private static final String TAG = WidgetUpdateService.class.getSimpleName();

    public WidgetUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockHawkWidget.class));

        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent localIntent = new Intent(this, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            //Load quotes info into widget view
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.quotes_widget);
            view.setRemoteAdapter(R.id.list_view, localIntent);
            view.setEmptyView(R.id.list_view, R.id.empty_view);

            Intent toastIntent = new Intent(this, StockHawkWidget.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting TOAST_ACTION.
            toastIntent.setAction(StockHawkWidget.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(this, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            view.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], view);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.list_view);
        }
    }
}
