package io.endigo.plugins.pdfviewflutter;


import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodCall;

/** FlutterPdfViewerPlugin */
public class PDFViewFlutterPlugin implements FlutterPlugin, MethodCallHandler {
  private MethodChannel channel;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

    BinaryMessenger binaryMessenger =  flutterPluginBinding.getBinaryMessenger();
    channel = new MethodChannel(binaryMessenger, "plugins.endigo.io/pdfview");
    channel.setMethodCallHandler(this);

    flutterPluginBinding.getPlatformViewRegistry().registerViewFactory("plugins.endigo.io/pdfview", new NativeViewFactory(binaryMessenger ,channel));
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
   }

   @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    // if (call.method.equals("getPlatformVersion")) {
    //   result.success("Android " + android.os.Build.VERSION.RELEASE);
    // } else {
    //   result.notImplemented();
    // }
  }
}
