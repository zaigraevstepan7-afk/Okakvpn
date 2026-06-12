package com.pingsecure.client.app.handlers;

import android.content.SharedPreferences;
import androidx.lifecycle.LifecycleCoroutineScope;
import com.pingsecure.client.app.Application;
import com.pingsecure.client.app.Settings;
import com.pingsecure.client.app.utils.SafeResult;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.systemchannels.RestorationChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.io.File;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.JobKt;
import okio.Okio;

/* loaded from: classes7.dex */
public final class MethodHandler implements FlutterPlugin, MethodChannel.MethodCallHandler {
    public MethodChannel channel;
    public volatile boolean isStopInProgress;
    public final LifecycleCoroutineScope scope;

    public MethodHandler(LifecycleCoroutineScope scope) {
        Intrinsics.checkNotNullParameter(scope, "scope");
        this.scope = scope;
    }

    public static final String access$initDirectory(MethodHandler methodHandler, String str, File file) {
        methodHandler.getClass();
        if ((str != null && str.length() != 0) || file == null) {
            return str == null ? "" : str;
        }
        file.mkdir();
        String path = file.getPath();
        Application application = Application.application;
        if (file.equals(Okio.getApplication().getFilesDir())) {
            SharedPreferences preferences = Settings.getPreferences();
            Intrinsics.checkNotNullExpressionValue(preferences, "<get-preferences>(...)");
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("flutter.base_path", path);
            edit.apply();
        } else if (file.equals(Okio.getApplication().getExternalFilesDir(null))) {
            SharedPreferences preferences2 = Settings.getPreferences();
            Intrinsics.checkNotNullExpressionValue(preferences2, "<get-preferences>(...)");
            SharedPreferences.Editor edit2 = preferences2.edit();
            edit2.putString("flutter.work_path", path);
            edit2.apply();
        } else if (file.equals(Okio.getApplication().getCacheDir())) {
            SharedPreferences preferences3 = Settings.getPreferences();
            Intrinsics.checkNotNullExpressionValue(preferences3, "<get-preferences>(...)");
            SharedPreferences.Editor edit3 = preferences3.edit();
            edit3.putString("flutter.temp_path", path);
            edit3.apply();
        }
        Intrinsics.checkNotNull(path);
        return path;
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Intrinsics.checkNotNullParameter(flutterPluginBinding, "flutterPluginBinding");
        MethodChannel methodChannel = new MethodChannel(flutterPluginBinding.binaryMessenger, "com.pingsecure.client.app/method");
        this.channel = methodChannel;
        methodChannel.setMethodCallHandler(this);
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onDetachedFromEngine(FlutterPlugin.FlutterPluginBinding binding) {
        Intrinsics.checkNotNullParameter(binding, "binding");
        MethodChannel methodChannel = this.channel;
        if (methodChannel != null) {
            methodChannel.setMethodCallHandler(null);
        }
    }

    @Override // io.flutter.plugin.common.MethodChannel.MethodCallHandler
    public final void onMethodCall(MethodCall call, MethodChannel.Result result) {
        Intrinsics.checkNotNullParameter(call, "call");
        SafeResult safeResult = new SafeResult((RestorationChannel.C175631) result);
        MethodHandler$Companion$Trigger[] methodHandler$Companion$TriggerArr = MethodHandler$Companion$Trigger.$VALUES;
        String str = call.method;
        boolean areEqual = Intrinsics.areEqual(str, "init");
        LifecycleCoroutineScope lifecycleCoroutineScope = this.scope;
        if (areEqual) {
            JobKt.launch$default(lifecycleCoroutineScope, null, null, new MethodHandler$handleInit$1(this, safeResult, null), 3);
            return;
        }
        if (Intrinsics.areEqual(str, "start")) {
            JobKt.launch$default(lifecycleCoroutineScope, null, null, new MethodHandler$handleStart$1(this, call, safeResult, null), 3);
            return;
        }
        if (Intrinsics.areEqual(str, "stop")) {
            JobKt.launch$default(lifecycleCoroutineScope, null, null, new MethodHandler$handleStop$1(this, safeResult, null), 3);
            return;
        }
        if (Intrinsics.areEqual(str, "get_logs")) {
            JobKt.launch$default(lifecycleCoroutineScope, null, null, new MethodHandler$handleGetLogs$1(safeResult, null), 3);
        } else if (Intrinsics.areEqual(str, "clear_logs")) {
            JobKt.launch$default(lifecycleCoroutineScope, null, null, new MethodHandler$handleClearLogs$1(safeResult, null), 3);
        } else {
            safeResult.notImplemented();
        }
    }
}
