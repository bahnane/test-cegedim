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

            Optional<Appointment> overlappingAppointment = findOverlappingAppointment(dateTimePlus15Minutes,
                    appointments);

            if (overlappingAppointment.isPresent() && isDateTimeWithinAppointment(localDateTime.plusMinutes(15),
                    overlappingAppointment.get())) {
                localDateTime = overlappingAppointment.get().getEndDate();
            } else {
                Availability availability = createAvailability(practitionerId, localDateTime);
                if (isAvailabilityValid(appointments, availability)) {
                    availabilities.add(availability);
                }
                localDateTime = localDateTime.plusMinutes(15);
            }

        }
        return availabilities;
    }

    private static Availability createAvailability(Integer practitionerId, LocalDateTime startDate) {
        return Availability.builder()
                           .practitionerId(practitionerId)
                           .startDate(startDate)
                           .endDate(startDate.plusMinutes(15))
                           .build();
    }

    private static Optional<Appointment> findOverlappingAppointment(LocalDateTime dateTime,
                                                                    List<Appointment> appointments) {
        return appointments.stream()
                           .filter(appointment -> isDateTimeWithinAppointment(dateTime, appointment))
                           .findFirst();
    }

    private static boolean isDateTimeWithinAppointment(LocalDateTime dateTime, Appointment appointment) {
        return dateTime.isAfter(appointment.getStartDate()) && dateTime.isBefore(appointment.getEndDate());
    }

    private static boolean isAvailabilityValid(List<Appointment> appointments, Availability availability) {
        return appointments.stream().noneMatch((appointment) -> isAppointmentAlreadyBooked(availability,
                appointment));
    }

    private static boolean isAppointmentAlreadyBooked(Availability availability, Appointment appointment) {
        return appointment.getStartDate().isEqual(
                availability.getStartDate()) && appointment.getEndDate().isEqual(availability.getEndDate());
    }
}
