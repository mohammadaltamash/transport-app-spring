package com.transport.app.rest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class AuditResponse {

    //    private Integer id;
    private long revision;
    private String userName;
    private String fullName;
    private String operation;
    private List<PropertyValue> changedProperties;
    private long timestamp;

    @Getter
    @Setter
//    @Builder
    public static class PropertyValue {
        private String propertyName;
        private String formattedPropertyName;
        private Object value;
        private Object previousValue;
    }
}
