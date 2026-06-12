package com.pingsecure.client.app.ads;

import androidx.fragment.app.Fragment$$ExternalSyntheticOutline0;
import androidx.room.Room$$ExternalSyntheticOutline0;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final class AdSlot {
    public static final List CONNECT_KEYS_IN_ORDER = CollectionsKt__CollectionsKt.listOf((Object[]) new String[]{"connectPrimary", "connectBackup"});
    public static final List DISCONNECT_KEYS_IN_ORDER = CollectionsKt__CollectionsKt.listOf((Object[]) new String[]{"disconnectPrimary", "disconnectBackup"});
    public final String adUnitId;
    public final boolean isBlocking;
    public final String key;
    public final String statId;

    public AdSlot(String str, String str2, String str3, boolean z) {
        this.key = str;
        this.adUnitId = str2;
        this.statId = str3;
        this.isBlocking = z;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdSlot)) {
            return false;
        }
        AdSlot adSlot = (AdSlot) obj;
        return Intrinsics.areEqual(this.key, adSlot.key) && Intrinsics.areEqual(this.adUnitId, adSlot.adUnitId) && Intrinsics.areEqual(this.statId, adSlot.statId) && this.isBlocking == adSlot.isBlocking;
    }

    public final int hashCode() {
        return Boolean.hashCode(this.isBlocking) + Fragment$$ExternalSyntheticOutline0.m355m(Fragment$$ExternalSyntheticOutline0.m355m(this.key.hashCode() * 31, 31, this.adUnitId), 31, this.statId);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("AdSlot(key=");
        sb.append(this.key);
        sb.append(", adUnitId=");
        sb.append(this.adUnitId);
        sb.append(", statId=");
        sb.append(this.statId);
        sb.append(", isBlocking=");
        return Room$$ExternalSyntheticOutline0.m640m(sb, this.isBlocking, ')');
    }
}
