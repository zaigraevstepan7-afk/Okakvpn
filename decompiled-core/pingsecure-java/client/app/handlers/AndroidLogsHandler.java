package com.pingsecure.client.app.handlers;

import androidx.lifecycle.LifecycleCoroutineScope;
import com.pingsecure.client.app.utils.SafeResult;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.systemchannels.RestorationChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.JobKt;

/* loaded from: classes7.dex */
public final class AndroidLogsHandler implements FlutterPlugin, MethodChannel.MethodCallHandler {
    public MethodChannel channel;
    public final LifecycleCoroutineScope scope;

    public AndroidLogsHandler(LifecycleCoroutineScope scope) {
        Intrinsics.checkNotNullParameter(scope, "scope");
        this.scope = scope;
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding binding) {
        Intrinsics.checkNotNullParameter(binding, "binding");
        MethodChannel methodChannel = new MethodChannel(binding.binaryMessenger, "com.pingsecure.client.app/android_logs");
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
        AndroidLogsHandler$Companion$Trigger[] androidLogsHandler$Companion$TriggerArr = AndroidLogsHandler$Companion$Trigger.$VALUES;
        if (!Intrinsics.areEqual(call.method, "get_android_logs")) {
            safeResult.notImplemented();
        } else {
            JobKt.launch$default(this.scope, null, null, new AndroidLogsHandler$handleGetAndroidLogs$1(safeResult, null), 3);
        }
    }
}
