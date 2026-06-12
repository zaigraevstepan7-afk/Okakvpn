package com.pingsecure.client.app.ads;

import android.app.Activity;
import android.util.Log;
import androidx.collection.ArraySet$$ExternalSyntheticOutline0;
import com.chartboost.sdk.impl.C3354b9;
import com.ironsource.sdk.controller.v$$ExternalSyntheticLambda3;
import com.yandex.mobile.ads.common.AdRequestConfiguration;
import com.yandex.mobile.ads.common.AdTheme;
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.MapsKt__MapsJVMKt;

/* loaded from: classes7.dex */
public final class YandexAdsLoader {
    public final Activity activity;
    public boolean anyBlockingLoaded;
    public final ConcurrentHashMap interstitials;
    public boolean isInitializedPosted;
    public final LinkedHashMap loaders;
    public int pendingLoadCount;
    public final LinkedHashMap slotsByKey;
    public final AdTheme theme;

    public YandexAdsLoader(Activity activity, boolean z, ArrayList arrayList) {
        this.activity = activity;
        this.theme = z ? AdTheme.DARK : AdTheme.LIGHT;
        int mapCapacity = MapsKt__MapsJVMKt.mapCapacity(CollectionsKt__IterablesKt.collectionSizeOrDefault(arrayList, 10));
        LinkedHashMap linkedHashMap = new LinkedHashMap(mapCapacity < 16 ? 16 : mapCapacity);
        for (Object obj : arrayList) {
            linkedHashMap.put(((AdSlot) obj).key, obj);
        }
        this.slotsByKey = linkedHashMap;
        this.loaders = new LinkedHashMap();
        this.interstitials = new ConcurrentHashMap();
    }

    public static final void access$maybePostInitialized(YandexAdsLoader yandexAdsLoader, v$$ExternalSyntheticLambda3 v__externalsyntheticlambda3) {
        if (yandexAdsLoader.isInitializedPosted) {
            return;
        }
        if (yandexAdsLoader.anyBlockingLoaded || yandexAdsLoader.pendingLoadCount <= 0) {
            yandexAdsLoader.isInitializedPosted = true;
            v__externalsyntheticlambda3.invoke(Boolean.valueOf(!yandexAdsLoader.interstitials.isEmpty()));
        }
    }

    public final void initialize(v$$ExternalSyntheticLambda3 v__externalsyntheticlambda3) {
        LinkedHashMap linkedHashMap = this.slotsByKey;
        if (linkedHashMap.isEmpty()) {
            v__externalsyntheticlambda3.invoke(Boolean.FALSE);
            return;
        }
        Collection values = linkedHashMap.values();
        int i = 0;
        if (!(values instanceof Collection) || !values.isEmpty()) {
            Iterator it = values.iterator();
            while (it.hasNext()) {
                if (((AdSlot) it.next()).isBlocking && (i = i + 1) < 0) {
                    CollectionsKt__CollectionsKt.throwCountOverflow();
                    throw null;
                }
            }
        }
        this.pendingLoadCount = i;
        if (i == 0) {
            this.isInitializedPosted = true;
            v__externalsyntheticlambda3.invoke(Boolean.FALSE);
        }
        for (AdSlot adSlot : linkedHashMap.values()) {
            InterstitialAdLoader interstitialAdLoader = new InterstitialAdLoader(this.activity);
            interstitialAdLoader.setAdLoadListener(new C3354b9(adSlot, this, v__externalsyntheticlambda3, 28));
            this.loaders.put(adSlot.key, interstitialAdLoader);
            loadSlot(adSlot);
        }
    }

    public final void loadSlot(AdSlot adSlot) {
        LinkedHashMap linkedHashMap = this.loaders;
        String str = adSlot.key;
        InterstitialAdLoader interstitialAdLoader = (InterstitialAdLoader) linkedHashMap.get(str);
        if (interstitialAdLoader == null) {
            Log.d("YandexAdsLoader", "loadSlot SKIPPED — no loader for slot=".concat(str));
            return;
        }
        StringBuilder m71m = ArraySet$$ExternalSyntheticOutline0.m71m("Calling loadAd (slot=", str, ", adUnitId=");
        String str2 = adSlot.adUnitId;
        m71m.append(str2);
        m71m.append(", statId=");
        String str3 = adSlot.statId;
        m71m.append(str3);
        m71m.append(')');
        Log.d("YandexAdsLoader", m71m.toString());
        interstitialAdLoader.loadAd(new AdRequestConfiguration.Builder(str2).setPreferredTheme(this.theme).setParameters(MapsKt__MapsJVMKt.mapOf(new Pair("stat-id", str3))).build());
    }
}
