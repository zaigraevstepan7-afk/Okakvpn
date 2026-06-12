package com.pingsecure.client.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.lifecycle.LifecycleOwnerKt;
import com.chartboost.sdk.impl.C3381g;
import com.p232my.target.j0$$ExternalSyntheticLambda0;
import com.pingsecure.client.app.handlers.AdsMethodHandler;
import com.pingsecure.client.app.handlers.AndroidLogsHandler;
import com.pingsecure.client.app.handlers.MethodHandler;
import com.pingsecure.client.app.handlers.MobileInfoMethodHandler;
import com.pingsecure.client.app.handlers.PlatformSettingsHandler;
import com.pingsecure.client.app.handlers.ReviewHandler;
import com.pingsecure.client.app.handlers.StatsStreamHandler;
import com.pingsecure.client.app.handlers.UserIdMethodHandler;
import com.pingsecure.client.app.handlers.VPNStateStreamHandler;
import io.flutter.embedding.android.FlutterFragmentActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineConnectionRegistry;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class MainActivity extends FlutterFragmentActivity {
    public static MainActivity instance;
    public static C3381g serviceStartCallback;
    public final ActivityResultLauncher prepareLauncher = registerForActivityResult(new PrepareService(), new j0$$ExternalSyntheticLambda0(this, 17));

    public final class PrepareService extends ActivityResultContract {
        @Override // androidx.activity.result.contract.ActivityResultContract
        public final Intent createIntent(Context context, Object obj) {
            Intent input = (Intent) obj;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(input, "input");
            return input;
        }

        @Override // androidx.activity.result.contract.ActivityResultContract
        public final Object parseResult(int i, Intent intent) {
            return Boolean.valueOf(i == -1);
        }
    }

    @Override // io.flutter.embedding.android.FlutterFragmentActivity, io.flutter.embedding.android.FlutterEngineConfigurator
    public final void configureFlutterEngine(FlutterEngine flutterEngine) {
        Intrinsics.checkNotNullParameter(flutterEngine, "flutterEngine");
        super.configureFlutterEngine(flutterEngine);
        instance = this;
        UserIdMethodHandler userIdMethodHandler = new UserIdMethodHandler();
        FlutterEngineConnectionRegistry flutterEngineConnectionRegistry = flutterEngine.pluginRegistry;
        flutterEngineConnectionRegistry.add(userIdMethodHandler);
        flutterEngineConnectionRegistry.add(new MobileInfoMethodHandler());
        flutterEngineConnectionRegistry.add(new MethodHandler(LifecycleOwnerKt.getLifecycleScope(this)));
        flutterEngineConnectionRegistry.add(new AndroidLogsHandler(LifecycleOwnerKt.getLifecycleScope(this)));
        flutterEngineConnectionRegistry.add(new PlatformSettingsHandler());
        flutterEngineConnectionRegistry.add(new ReviewHandler(LifecycleOwnerKt.getLifecycleScope(this)));
        flutterEngineConnectionRegistry.add(new StatsStreamHandler());
        flutterEngineConnectionRegistry.add(new VPNStateStreamHandler());
        flutterEngineConnectionRegistry.add(new AdsMethodHandler());
    }

    @Override // io.flutter.embedding.android.FlutterFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        EdgeToEdge.enable$default(this, null, null, 3, null);
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onDestroy() {
        serviceStartCallback = null;
        super.onDestroy();
    }
}
