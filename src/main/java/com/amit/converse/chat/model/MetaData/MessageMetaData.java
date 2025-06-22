package com.amit.converse.chat.model.MetaData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@SuperBuilder
public abstract class MessageMetaData {

    private boolean isEncrypted;
    @Field("deletedForUsers")
    private Set<String> deletedForUsers;

    public MessageMetaData() {
        this.isEncrypted = false;
        this.deletedForUsers = new HashSet<>();
    }

    public void addUserToDeletedForUsers(String userId) {
        deletedForUsers.add(userId);
    }

    public Integer getDeletedForUsersCount() {
        return deletedForUsers.size();
    }
}
