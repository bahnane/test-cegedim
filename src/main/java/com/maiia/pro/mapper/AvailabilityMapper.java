package com.maiia.pro.mapper;

import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityMapper {
    public static List<Availability> toAvailabilitiesFrom(TimeSlot timeSlot, Integer practitionerId) {
        List<Availability> availabilities = new ArrayList<>();
        LocalDateTime localDateTime = timeSlot.getStartDate();
        while (localDateTime.isBefore(timeSlot.getEndDate())) {
            availabilities.add(
                    Availability.builder().practitionerId(practitionerId).startDate(localDateTime)
                                .endDate(localDateTime.plusMinutes(15)).build());
            localDateTime = localDateTime.plusMinutes(15);
        }
        return availabilities;
    }
}
