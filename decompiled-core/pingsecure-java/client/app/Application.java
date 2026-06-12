package com.pingsecure.client.app;

import android.content.Context;
import com.ironsource.mediationsdk.u$a$$ExternalSyntheticLambda0;
import kotlin.SynchronizedLazyImpl;
import okio.Okio;
import p243go.Seq;

/* loaded from: classes7.dex */
public class Application extends android.app.Application {
    public static Application application;
    public static String lastCreatedActivity;
    public static final SynchronizedLazyImpl notification$delegate = Okio.lazy(new u$a$$ExternalSyntheticLambda0(1));
    public static final SynchronizedLazyImpl notificationManager$delegate;
    public static final SynchronizedLazyImpl packageManager$delegate;

    static {
        Okio.lazy(new u$a$$ExternalSyntheticLambda0(2));
        packageManager$delegate = Okio.lazy(new u$a$$ExternalSyntheticLambda0(3));
        Okio.lazy(new u$a$$ExternalSyntheticLambda0(4));
        notificationManager$delegate = Okio.lazy(new u$a$$ExternalSyntheticLambda0(5));
        Okio.lazy(new u$a$$ExternalSyntheticLambda0(6));
        lastCreatedActivity = "";
    }

    @Override // android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        application = this;
    }

    @Override // android.app.Application
    public final void onCreate() {
        super.onCreate();
        Seq.setContext((Context) this);
    }
}
