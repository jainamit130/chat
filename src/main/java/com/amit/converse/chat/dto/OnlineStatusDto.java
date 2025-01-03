package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnlineStatusDto {
    private String username;
    private ConnectionStatus status;
}
