package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class MethodHandler$Companion$Trigger {
    public static final /* synthetic */ MethodHandler$Companion$Trigger[] $VALUES;

    static {
        MethodHandler$Companion$Trigger[] methodHandler$Companion$TriggerArr = {new MethodHandler$Companion$Trigger("Init", 0), new MethodHandler$Companion$Trigger("Start", 1), new MethodHandler$Companion$Trigger("Stop", 2), new MethodHandler$Companion$Trigger("GetLogs", 3), new MethodHandler$Companion$Trigger("ClearLogs", 4)};
        $VALUES = methodHandler$Companion$TriggerArr;
        RandomKt.enumEntries(methodHandler$Companion$TriggerArr);
    }

    public static MethodHandler$Companion$Trigger valueOf(String str) {
        return (MethodHandler$Companion$Trigger) Enum.valueOf(MethodHandler$Companion$Trigger.class, str);
    }

    public static MethodHandler$Companion$Trigger[] values() {
        return (MethodHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
