package com.maiia.pro.helper;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.TimeSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AvailabilityHelper {
    public static List<Availability> toAvailabilitiesFrom(TimeSlot timeSlot,
                                                          List<Appointment> appointments,
                                                          Integer practitionerId) {

        List<Availability> availabilities = new ArrayList<>();
        LocalDateTime localDateTime = timeSlot.getStartDate();

        while (localDateTime.isBefore(timeSlot.getEndDate())) {
            LocalDateTime dateTimePlus15Minutes = localDateTime.plusMinutes(15);

            Optional<Appointment> overlappingAppointment = appointments.stream()
                                                                       .filter((appointment) -> isDateTimeWithinAppointment(
                                                                               dateTimePlus15Minutes,
                                                                               appointment))
                                                                       .findFirst();

            if (overlappingAppointment.isPresent() && isDateTimeWithinAppointment(localDateTime.plusMinutes(15),
                    overlappingAppointment.get())) {
                localDateTime = overlappingAppointment.get().getEndDate();
            } else {
                availabilities.add(
                        Availability.builder().practitionerId(practitionerId).startDate(localDateTime)
                                    .endDate(localDateTime.plusMinutes(15)).build());
                localDateTime = localDateTime.plusMinutes(15);
            }

        }
        return availabilities;
    }

    private static boolean isDateTimeWithinAppointment(LocalDateTime dateTime, Appointment appointment) {
        return dateTime.isAfter(appointment.getStartDate()) && dateTime.isBefore(appointment.getEndDate());
    }
}
