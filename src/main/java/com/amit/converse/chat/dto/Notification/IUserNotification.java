package com.amit.converse.chat.dto.Notification;

public abstract class IUserNotification implements INotification {
    protected NotificationType notificationType;

    public IUserNotification() {
        this.notificationType = getNotificationType();
    }
}
