package com.pingsecure.client.app;

import android.content.SharedPreferences;
import com.ironsource.mediationsdk.u$a$$ExternalSyntheticLambda0;
import kotlin.SynchronizedLazyImpl;
import okio.Okio;

/* loaded from: classes7.dex */
public abstract class Settings {
    public static final SynchronizedLazyImpl preferences$delegate = Okio.lazy(new u$a$$ExternalSyntheticLambda0(7));

    public static SharedPreferences getPreferences() {
        return (SharedPreferences) preferences$delegate.getValue();
    }
}
