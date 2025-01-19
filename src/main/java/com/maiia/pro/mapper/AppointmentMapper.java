package com.maiia.pro.mapper;

import com.maiia.pro.dto.AppointmentDTO;
import com.maiia.pro.entity.Appointment;

public class AppointmentMapper {
    public static Appointment toAppointmentEntityFrom(AppointmentDTO appointmentDTO) {
        return Appointment.builder().practitionerId(appointmentDTO.getPractitionerId())
                          .patientId(appointmentDTO.getPatientId())
                          .startDate(appointmentDTO.getStartDate()).endDate(appointmentDTO.getEndDate()).build();
    }
}
