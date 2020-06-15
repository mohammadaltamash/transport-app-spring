package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.Preferences;
import com.transport.app.rest.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class PreferencesMapper {

    public static Preferences toPreferences(PreferencesDto preferencesDto) {
        return Preferences.builder()
                .id(preferencesDto.getId())
                .termsAndConditions(preferencesDto.getTermsAndConditions())
                .build();
    }

    public static PreferencesDto toPreferencesDto(Preferences preferences) {
        if (preferences == null) {
            return null;
        }
        return PreferencesDto.builder()
                .id(preferences.getId())
                .termsAndConditions(preferences.getTermsAndConditions())
                .build();
    }

    public static Preferences toUpdatedPreferences(Preferences preferences, Preferences preferencesUpdate) {
        preferences.setId(preferencesUpdate.getId() == null ? preferences.getId() : preferencesUpdate.getId());
        preferences.setTermsAndConditions(preferencesUpdate.getTermsAndConditions() == null ? preferences.getTermsAndConditions() : preferencesUpdate.getTermsAndConditions());
        return preferences;
    }
}
