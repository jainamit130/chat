package com.amit.converse.chat.dto.Notification;

public abstract class IChatNotification implements INotification{
    protected NotificationType notificationType;

    public IChatNotification() {
        this.notificationType = getNotificationType();
    }
}
