package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class AndroidLogsHandler$Companion$Trigger {
    public static final /* synthetic */ AndroidLogsHandler$Companion$Trigger[] $VALUES;

    static {
        AndroidLogsHandler$Companion$Trigger[] androidLogsHandler$Companion$TriggerArr = {new AndroidLogsHandler$Companion$Trigger("GetAndroidLogs", 0)};
        $VALUES = androidLogsHandler$Companion$TriggerArr;
        RandomKt.enumEntries(androidLogsHandler$Companion$TriggerArr);
    }

    public static AndroidLogsHandler$Companion$Trigger valueOf(String str) {
        return (AndroidLogsHandler$Companion$Trigger) Enum.valueOf(AndroidLogsHandler$Companion$Trigger.class, str);
    }

    public static AndroidLogsHandler$Companion$Trigger[] values() {
        return (AndroidLogsHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
