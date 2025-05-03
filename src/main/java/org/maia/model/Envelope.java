package org.maia.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.maia.enums.EventType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envelope {
    private EventType eventType;
    private String data;
}
