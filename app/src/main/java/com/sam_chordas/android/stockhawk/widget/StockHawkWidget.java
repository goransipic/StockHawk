package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.DetailStocksActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by User on 15.1.2017..
 */

public class StockHawkWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {

            Intent myStocksActivity = new Intent(context, MyStocksActivity.class);
            Intent detailStocksActivity = new Intent(context, DetailStocksActivity.class);

            Cursor cursor = context.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);

            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);

            cursor.moveToPosition(viewIndex);

            String symbol = cursor.getString(cursor.getColumnIndex("symbol"));
            detailStocksActivity.putExtra(QuoteColumns.SYMBOL,symbol);

            cursor.close();

            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntent(myStocksActivity)
                    .addNextIntent(detailStocksActivity)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                clickPendingIntentTemplate.send(context,0,new Intent());
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

            //Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the background service to update the widgets
        context.startService(new Intent(context, WidgetUpdateService.class));
    }

}
