package com.pingsecure.client.app.handlers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.firebase.tracing.ComponentMonitor;
import com.p232my.target.g7$$ExternalSyntheticLambda1;
import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.C17564x83f374f7;
import io.flutter.plugin.common.EventChannel$StreamHandler;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

/* loaded from: classes7.dex */
public final class StatsStreamHandler implements FlutterPlugin, EventChannel$StreamHandler {
    public static final ComponentMonitor Companion = new ComponentMonitor(24);
    public static volatile StatsStreamHandler INSTANCE;
    public FlutterInjector channel;
    public C17564x83f374f7 eventSink;
    public final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Intrinsics.checkNotNullParameter(flutterPluginBinding, "flutterPluginBinding");
        FlutterInjector flutterInjector = new FlutterInjector(flutterPluginBinding.binaryMessenger, "com.pingsecure.client.app/stats");
        this.channel = flutterInjector;
        flutterInjector.setStreamHandler(Companion.getShared());
    }

    @Override // io.flutter.plugin.common.EventChannel$StreamHandler
    public final void onCancel() {
        this.eventSink = null;
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onDetachedFromEngine(FlutterPlugin.FlutterPluginBinding binding) {
        Intrinsics.checkNotNullParameter(binding, "binding");
        FlutterInjector flutterInjector = this.channel;
        if (flutterInjector != null) {
            flutterInjector.setStreamHandler(null);
        }
        this.channel = null;
        this.eventSink = null;
    }

    @Override // io.flutter.plugin.common.EventChannel$StreamHandler
    public final void onListen(C17564x83f374f7 c17564x83f374f7) {
        this.eventSink = c17564x83f374f7;
        sendStats(0L, 0L, 0L, 0L);
    }

    public final void sendStats(long j, long j2, long j3, long j4) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("downlinkTotal", j);
            jSONObject.put("uplinkTotal", j2);
            jSONObject.put("downlink", j3);
            jSONObject.put("uplink", j4);
            this.mainHandler.post(new g7$$ExternalSyntheticLambda1(23, this, jSONObject));
        } catch (Exception e) {
            Log.e("StatsStreamHandler", "Failed to send stats: " + e.getMessage(), e);
        }
    }
}
