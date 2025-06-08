package com.amit.converse.chat.model.ChatRooms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlindPeriod {
    private Instant start;
    private Instant end;
}
