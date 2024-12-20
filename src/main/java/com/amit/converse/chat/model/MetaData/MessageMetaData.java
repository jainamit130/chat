package com.amit.converse.chat.model.MetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public abstract class MessageMetaData {
    @Builder.Default
    private boolean isEncrypted = false;

    @Builder.Default
    private Set<String> deletedForUsers = new HashSet<>();

    public abstract void readMessage

    public void addUserToDeletedForUsers(String userId) {
        deletedForUsers.add(userId);
    }
}
