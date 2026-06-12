package com.pingsecure.client.app.handlers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.lifecycle.LifecycleCoroutineScope;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.zzd;
import com.pingsecure.client.app.MainActivity;
import com.pingsecure.client.app.utils.SafeResult;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.systemchannels.RestorationChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.JobKt;
import yads.aa2$$ExternalSyntheticLambda0;

/* loaded from: classes7.dex */
public final class ReviewHandler implements FlutterPlugin, MethodChannel.MethodCallHandler {
    public MethodChannel channel;
    public final LifecycleCoroutineScope scope;

    public ReviewHandler(LifecycleCoroutineScope scope) {
        Intrinsics.checkNotNullParameter(scope, "scope");
        this.scope = scope;
    }

    public static final void access$openGoogleDialog(ReviewHandler reviewHandler) {
        reviewHandler.getClass();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        Intrinsics.checkNotNullExpressionValue(googleApiAvailability, "getInstance(...)");
        MainActivity mainActivity = MainActivity.instance;
        if (googleApiAvailability.isGooglePlayServicesAvailable(ResultKt.getInstance()) != 0) {
            openGP(3);
            return;
        }
        zzd create = ResultKt.create(ResultKt.getInstance());
        Task requestReviewFlow = create.requestReviewFlow();
        Intrinsics.checkNotNullExpressionValue(requestReviewFlow, "requestReviewFlow(...)");
        Intrinsics.checkNotNull(requestReviewFlow.addOnCompleteListener(new aa2$$ExternalSyntheticLambda0(create, reviewHandler, new boolean[]{false}, 12)));
    }

    public static void openGP(int i) {
        MainActivity mainActivity = MainActivity.instance;
        MainActivity resultKt = ResultKt.getInstance();
        String packageName = resultKt.getPackageName();
        try {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse("market://details?id=" + packageName));
                intent.setPackage("com.android.vending");
                intent.addFlags(268435456);
                resultKt.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                intent2.addFlags(268435456);
                resultKt.startActivity(intent2);
            }
        } catch (Exception e) {
            Toast.makeText(resultKt, "Error opening Google Play", 0).show();
            if (i == 1) {
                System.out.println((Object) "Error while open review dialog");
            } else if (i == 2) {
                System.out.println((Object) "Error while get ReviewInfo");
            } else if (i == 3) {
                System.out.println((Object) "Google Play Services is not available");
            }
            e.printStackTrace();
        }
    }

    @Override // io.flutter.embedding.engine.plugins.FlutterPlugin
    public final void onAttachedToEngine(FlutterPlugin.FlutterPluginBinding flutterPluginBinding) {
        Intrinsics.checkNotNullParameter(flutterPluginBinding, "flutterPluginBinding");
        MethodChannel methodChannel = new MethodChannel(flutterPluginBinding.binaryMessenger, "com.pingsecure.client.app/review");
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
        ReviewHandler$Companion$Trigger[] reviewHandler$Companion$TriggerArr = ReviewHandler$Companion$Trigger.$VALUES;
        if (!Intrinsics.areEqual(call.method, "show_review")) {
            safeResult.notImplemented();
        } else {
            JobKt.launch$default(this.scope, null, null, new ReviewHandler$onMethodCall$1(this, safeResult, null), 3);
        }
    }
}
