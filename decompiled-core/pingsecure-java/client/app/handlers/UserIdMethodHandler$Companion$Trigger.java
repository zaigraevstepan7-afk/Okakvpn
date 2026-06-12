package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class UserIdMethodHandler$Companion$Trigger {
    public static final /* synthetic */ UserIdMethodHandler$Companion$Trigger[] $VALUES;

    static {
        UserIdMethodHandler$Companion$Trigger[] userIdMethodHandler$Companion$TriggerArr = {new UserIdMethodHandler$Companion$Trigger("CheckPreviousId", 0)};
        $VALUES = userIdMethodHandler$Companion$TriggerArr;
        RandomKt.enumEntries(userIdMethodHandler$Companion$TriggerArr);
    }

    public static UserIdMethodHandler$Companion$Trigger valueOf(String str) {
        return (UserIdMethodHandler$Companion$Trigger) Enum.valueOf(UserIdMethodHandler$Companion$Trigger.class, str);
    }

    public static UserIdMethodHandler$Companion$Trigger[] values() {
        return (UserIdMethodHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
