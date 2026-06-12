package com.pingsecure.client.app.p233bg;

import android.content.Context;
import android.util.Log;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class TurnOffWorker extends Worker {
    public static final Companion Companion = new Companion(null);
    private static final String TAG = "TurnOffWorker";

    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TurnOffWorker(Context context, WorkerParameters params) {
        super(context, params);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(params, "params");
    }

    @Override // androidx.work.Worker
    public ListenableWorker.Result doWork() {
        try {
            Log.d(TAG, "TurnOffWorker: Sending ACTION_SERVICE_CLOSE");
            XrayService.Companion.stop();
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNull(success);
            return success;
        } catch (Exception e) {
            Log.e(TAG, "TurnOffWorker: Error: " + e.getMessage());
            ListenableWorker.Result failure = ListenableWorker.Result.failure();
            Intrinsics.checkNotNull(failure);
            return failure;
        }
    }
}
