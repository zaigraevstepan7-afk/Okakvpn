package com.pingsecure.client.app.handlers;

import android.os.Handler;
import android.os.Looper;
import com.google.firebase.tracing.ComponentMonitor;
import com.p232my.target.g7$$ExternalSyntheticLambda1;
import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.C17564x83f374f7;
import io.flutter.plugin.common.EventChannel$StreamHandler;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.RandomKt;

/* loaded from: classes7.dex */
public final class VPNStateStreamHandler implements FlutterPlugin, EventChannel$StreamHandler {
    public static final ComponentMonitor Companion = new ComponentMonitor(25);
    public static volatile VPNStateStreamHandler INSTANCE;
    public static volatile String currentStatus;
    public FlutterInjector channel;
    public C17564x83f374f7 eventSink;
    public final Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus = VPNStateStreamHandler$Companion$VpnStatus.CONNECTING;
        currentStatus = "Disconnected";
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Intrinsics.checkNotNullParameter(flutterPluginBinding, "flutterPluginBinding");
        FlutterInjector flutterInjector = new FlutterInjector(flutterPluginBinding.binaryMessenger, "com.pingsecure.client.app/vpn_state");
        this.channel = flutterInjector;
        flutterInjector.setStreamHandler(Companion.m55542getShared());
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
        String str;
        this.eventSink = c17564x83f374f7;
        if (RandomKt.isRunning) {
            str = currentStatus;
        } else {
            VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus = VPNStateStreamHandler$Companion$VpnStatus.CONNECTING;
            str = "Disconnected";
        }
        c17564x83f374f7.success(str);
    }

    public final void sendEvent(VPNStateStreamHandler$Companion$VpnStatus vPNStateStreamHandler$Companion$VpnStatus) {
        currentStatus = vPNStateStreamHandler$Companion$VpnStatus.value;
        this.mainHandler.post(new g7$$ExternalSyntheticLambda1(24, this, vPNStateStreamHandler$Companion$VpnStatus));
    }
}
