package com.amit.converse.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageInfoDto {
    private Map<String, Set<UserDetails>> deliveryReceiptsByTime;
    private Map<String, Set<UserDetails>> readReceiptsByTime;
}
