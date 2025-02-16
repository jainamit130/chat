package com.amit.converse.chat.model.MetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@SuperBuilder
public abstract class MessageMetaData {

    public MessageMetaData() {
        this.isEncrypted = false;
        this.deletedForUsers = new HashSet<>();
    }

    private boolean isEncrypted;
    private Set<String> deletedForUsers;

    public abstract Integer readMessage(String timestamp,String userId);

    public abstract Integer deliverMessage(String timestamp,String userId);

    public void addUserToDeletedForUsers(String userId) {
        deletedForUsers.add(userId);
    }

    public Integer getDeletedForUsersCount() {
        return deletedForUsers.size();
    }
}
