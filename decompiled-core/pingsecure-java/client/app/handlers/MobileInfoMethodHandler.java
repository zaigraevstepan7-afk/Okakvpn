package com.pingsecure.client.app.handlers;

import android.content.Context;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import androidx.core.animation.AnimatorKt$$ExternalSyntheticLambda0;
import androidx.media3.common.C0537C;
import com.pingsecure.client.app.utils.SafeResult;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.systemchannels.RestorationChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.security.MessageDigest;
import kotlin.Pair;
import kotlin.collections.ArraysKt;
import kotlin.collections.EmptyList;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* loaded from: classes7.dex */
public final class MobileInfoMethodHandler implements FlutterPlugin, MethodChannel.MethodCallHandler {
    public MethodChannel channel;
    public Context context;

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding binding) {
        Intrinsics.checkNotNullParameter(binding, "binding");
        MethodChannel methodChannel = new MethodChannel(binding.binaryMessenger, "com.pingsecure.client.app/mobile_info");
        this.channel = methodChannel;
        this.context = binding.applicationContext;
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

    /* JADX WARN: Removed duplicated region for block: B:11:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // io.flutter.plugin.common.MethodChannel.MethodCallHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void onMethodCall(MethodCall call, MethodChannel.Result result) {
        Signature[] signatureArr;
        String joinToString$default;
        SigningInfo signingInfo;
        Context context;
        String networkOperatorName;
        Intrinsics.checkNotNullParameter(call, "call");
        SafeResult safeResult = new SafeResult((RestorationChannel.C175631) result);
        MobileInfoMethodHandler$Companion$Trigger[] mobileInfoMethodHandler$Companion$TriggerArr = MobileInfoMethodHandler$Companion$Trigger.$VALUES;
        if (!Intrinsics.areEqual(call.method, "getMobileInfo")) {
            safeResult.notImplemented();
            return;
        }
        Context context2 = this.context;
        String str = "unavailable";
        if (context2 != null) {
            try {
                String packageName = context2.getPackageName();
                if (Build.VERSION.SDK_INT >= 28) {
                    signingInfo = context2.getPackageManager().getPackageInfo(packageName, C0537C.BUFFER_FLAG_FIRST_SAMPLE).signingInfo;
                    signatureArr = signingInfo != null ? signingInfo.getApkContentsSigners() : null;
                } else {
                    signatureArr = context2.getPackageManager().getPackageInfo(packageName, 64).signatures;
                }
            } catch (Exception unused) {
            }
            if (signatureArr != null) {
                Signature signature = signatureArr.length == 0 ? null : signatureArr[0];
                if (signature != null) {
                    byte[] digest = MessageDigest.getInstance("SHA-256").digest(signature.toByteArray());
                    Intrinsics.checkNotNull(digest);
                    joinToString$default = ArraysKt.joinToString$default(digest, "", new AnimatorKt$$ExternalSyntheticLambda0(3), 30);
                    Pair pair = new Pair("appSignatureHash", joinToString$default);
                    context = this.context;
                    if (context != null) {
                        try {
                            Object systemService = context.getSystemService("phone");
                            TelephonyManager telephonyManager = systemService instanceof TelephonyManager ? (TelephonyManager) systemService : null;
                            if (telephonyManager != null && (networkOperatorName = telephonyManager.getNetworkOperatorName()) != null) {
                                String str2 = StringsKt.isBlank(networkOperatorName) ? null : networkOperatorName;
                                if (str2 != null) {
                                    str = str2;
                                }
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    safeResult.success(MapsKt.mapOf(pair, new Pair("carrierName", str), new Pair("vpnPackages", EmptyList.INSTANCE)));
                }
            }
        }
        joinToString$default = "unavailable";
        Pair pair2 = new Pair("appSignatureHash", joinToString$default);
        context = this.context;
        if (context != null) {
        }
        safeResult.success(MapsKt.mapOf(pair2, new Pair("carrierName", str), new Pair("vpnPackages", EmptyList.INSTANCE)));
    }
}
