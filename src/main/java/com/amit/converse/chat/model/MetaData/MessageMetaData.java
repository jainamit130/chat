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
@NoArgsConstructor
@SuperBuilder
public abstract class MessageMetaData {
    @Builder.Default
    private boolean isEncrypted = false;

    @Builder.Default
    private Set<String> deletedForUsers = new HashSet<>();

    public abstract Integer readMessage(String timestamp,String userId);

    public abstract Integer deliverMessage(String timestamp,String userId);

    public void addUserToDeletedForUsers(String userId) {
        deletedForUsers.add(userId);
    }

    public Integer getDeletedForUsersCount() {
        return deletedForUsers.size();
    }
}
