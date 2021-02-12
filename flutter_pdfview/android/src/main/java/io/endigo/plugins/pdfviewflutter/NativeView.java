
package io.endigo.plugins.pdfviewflutter;

import android.net.Uri;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.flutter.plugin.platform.PlatformView;
import java.util.Map;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.PDFView.Configurator;
import com.github.barteksc.pdfviewer.listener.*;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;


class NativeView implements PlatformView, MethodCallHandler {
   @NonNull private final PDFView pdfView;

   @NonNull private final MethodChannel channel;

    NativeView(@NonNull Context context, int id, @Nullable Map<String, Object> params, MethodChannel channel ) {

        final NativeView me = this;
        this.channel = channel;

        me.channel.setMethodCallHandler(this);


        Configurator config = null;
        pdfView = new PDFView(context, null);
        if (params.get("filePath") != null) {
            String filePath = (String) params.get("filePath");
            config = pdfView.fromUri(getURI(filePath));
          }

          if (config != null) {
            config
            .swipeHorizontal(true)
            .pageSnap(true)
            .onPageChange(new OnPageChangeListener() {
                @Override
                public void onPageChanged(int page, int total) {
                    Map<String, Object> args = new HashMap<>();
                    args.put("page", page);
                    args.put("total", total);
                    android.util.Log.i("TTT", "onPageChanged:" + page);
                    me.channel.invokeMethod("onPageChanged", args);   
                }
            })
            .defaultPage(getInt(params, "defaultPage"))
            .load();
        }
        
    }
    

    @NonNull
    @Override
    public View getView() {
        return pdfView;
    }

    @Override
    public void dispose() {}


    private Uri getURI(final String uri) {
        Uri parsed = Uri.parse(uri);

        if (parsed.getScheme() == null || parsed.getScheme().isEmpty()) {
            return Uri.fromFile(new File(uri));
        }
        return parsed;
    }


    int getInt(Map<String, Object> params, String key) {
        return params.containsKey(key) ? (int) params.get(key) : 0;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall methodCall, @NonNull Result result) {
        switch (methodCall.method) {
            case "pageCount":
                getPageCount(result);
                Log.i("pageCount", result+"");
                break;
            case "currentPage":
                getCurrentPage(result);
                break;
            case "setPage":
                setPage(methodCall, result);
                break;
      
            default:
                result.notImplemented();
                break;
        }
    }

    void getPageCount(Result result) {
        result.success(pdfView.getPageCount());
    }

    void getCurrentPage(Result result) {
        result.success(pdfView.getCurrentPage());
    }

    void setPage(MethodCall call, Result result) {
        if (call.argument("page") != null) {
            int page = (int) call.argument("page");
            pdfView.jumpTo(page);
        }

        result.success(true);
    }

}