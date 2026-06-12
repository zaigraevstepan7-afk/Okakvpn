package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class MobileInfoMethodHandler$Companion$Trigger {
    public static final /* synthetic */ MobileInfoMethodHandler$Companion$Trigger[] $VALUES;

    static {
        MobileInfoMethodHandler$Companion$Trigger[] mobileInfoMethodHandler$Companion$TriggerArr = {new MobileInfoMethodHandler$Companion$Trigger("GetMobileInfo", 0), new MobileInfoMethodHandler$Companion$Trigger("GetVpnPackages", 1)};
        $VALUES = mobileInfoMethodHandler$Companion$TriggerArr;
        RandomKt.enumEntries(mobileInfoMethodHandler$Companion$TriggerArr);
    }

    public static MobileInfoMethodHandler$Companion$Trigger valueOf(String str) {
        return (MobileInfoMethodHandler$Companion$Trigger) Enum.valueOf(MobileInfoMethodHandler$Companion$Trigger.class, str);
    }

    public static MobileInfoMethodHandler$Companion$Trigger[] values() {
        return (MobileInfoMethodHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
