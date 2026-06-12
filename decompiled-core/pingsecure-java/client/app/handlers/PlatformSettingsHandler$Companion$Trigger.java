package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class PlatformSettingsHandler$Companion$Trigger {
    public static final /* synthetic */ PlatformSettingsHandler$Companion$Trigger[] $VALUES;

    static {
        PlatformSettingsHandler$Companion$Trigger[] platformSettingsHandler$Companion$TriggerArr = {new PlatformSettingsHandler$Companion$Trigger("RequestIgnoreBatteryOptimizations", 0), new PlatformSettingsHandler$Companion$Trigger("IsAutoStartAvailable", 1), new PlatformSettingsHandler$Companion$Trigger("RequestAutoStart", 2), new PlatformSettingsHandler$Companion$Trigger("GetInstalledPackages", 3), new PlatformSettingsHandler$Companion$Trigger("GetPackagesIcon", 4)};
        $VALUES = platformSettingsHandler$Companion$TriggerArr;
        RandomKt.enumEntries(platformSettingsHandler$Companion$TriggerArr);
    }

    public static PlatformSettingsHandler$Companion$Trigger valueOf(String str) {
        return (PlatformSettingsHandler$Companion$Trigger) Enum.valueOf(PlatformSettingsHandler$Companion$Trigger.class, str);
    }

    public static PlatformSettingsHandler$Companion$Trigger[] values() {
        return (PlatformSettingsHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
