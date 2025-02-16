package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.helper.AvailabilityHelper;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public List<Availability> findByPractitionerId(Integer practitionerId) {
        return availabilityRepository.findByPractitionerId(practitionerId);
    }

    public List<Availability> generateAvailabilities(Integer practitionerId) {
        List<Appointment> appointments = this.appointmentRepository.findByPractitionerId(practitionerId);
        return this.timeSlotRepository.findByPractitionerId(practitionerId).stream()
                                      .map((timeSlot) -> AvailabilityHelper.toAvailabilitiesFrom(timeSlot, appointments,
                                              practitionerId))
                                      .flatMap(Collection::stream)
                                      .collect(Collectors.toList());
    }

    public void saveAvailabilities(List<Availability> availabilities) {
        this.availabilityRepository.saveAll(availabilities);
    }
}
