package com.example.HMS.service.Impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.HMS.exception.BadRequestException;
import com.example.HMS.exception.UnauthorizedException;
import com.example.HMS.model.entities.Appointment;
import com.example.HMS.model.entities.Doctor;
import com.example.HMS.model.entities.DoctorSchedule;
import com.example.HMS.model.entities.Payment;
import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
import com.example.HMS.model.request.AppointmentRequest;
import com.example.HMS.model.request.ConfirmedAppointmentRequest;
import com.example.HMS.model.response.ApiResponse;
import com.example.HMS.model.response.AppointmentResposne;
import com.example.HMS.repository.AppointmentRepository;
import com.example.HMS.repository.DoctorRepository;
import com.example.HMS.repository.DoctorScheduleRepository;
import com.example.HMS.repository.PaymentRepository;
import com.example.HMS.repository.UserRepository;
import com.example.HMS.security.UserPrincipal;
import com.example.HMS.service.AppointmentService;
import com.example.HMS.service.EmailService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class AppointmentServiceImpl implements AppointmentService{
	
	private final UserRepository userRepository;
	
	private final DoctorRepository doctorRepository;
	
	private final AppointmentRepository appointmentRepository;
	
	private final DoctorScheduleRepository doctorScheduleRepository;
	
	private final EmailService emailService;
	
	private final PaymentRepository paymentRepository;

	@Override
	public ApiResponse bookAppointment(UserPrincipal currentUser, AppointmentRequest request) {
		
		User patient = userRepository.findByName(currentUser.getUsername()).orElseThrow(
				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
		 
		 System.out.print(patient.getName());
		 
		 Doctor doctor = doctorRepository.findById(request.getDoctorId())
		            .orElseThrow(() -> new RuntimeException("Doctor not found"));
		 
		 LocalDateTime dateTime;

		 try {
		    // This works for '2025-11-06T10:00'
		    dateTime = LocalDateTime.parse(request.getDateTime());
		 } catch (DateTimeParseException e) {
		    return new ApiResponse(false, "Invalid date-time format. Use: yyyy-MM-ddTHH:mm");
		 }
		 
		// Step 1: Get the doctor's schedule for that date
		 DoctorSchedule schedule = doctorScheduleRepository.findByDoctorIdAndDate(doctor.getId(), dateTime.toLocalDate());
		 if (schedule == null) {
		     return new ApiResponse(false, "Doctor is not available on this date.");
		 }
		    
		 // Step 2: Check if appointment time is within schedule
		 LocalTime appointmentTime = dateTime.toLocalTime();
		 if (appointmentTime.isBefore(schedule.getStartTime()) || appointmentTime.isAfter(schedule.getEndTime())) {
		     return new ApiResponse(false, "Appointment time is outside the doctor's available schedule.");
		 }

		 if(patient.getRole() == Role.USER) {
			 Appointment appointment = new Appointment();
			 
			 appointment.setPatient(patient);
			 appointment.setDoctor(doctor);
			 appointment.setDateTime(dateTime);
			 appointment.setConfirmed(false);
			 appointment.setCancelled(false);
			 
			 appointmentRepository.save(appointment);
			 
			 return new ApiResponse(true, "We will send Email if doctor confirm your appointment!");
		 }
		 
		return new ApiResponse(false, "Cannot Book Appointment! Please Create User Account");
		
	}

	@Override
	public ApiResponse updateAppointment(long id, ConfirmedAppointmentRequest request) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new BadRequestException("Appointment Not Found!"));
		System.out.print("In updateAppointment"+request.getConfirmed());
		if(appointment != null) {
			if(request.getConfirmed()==true) {
				appointment.setConfirmed(request.getConfirmed());
				appointment.setCancelled(false);
				
				appointmentRepository.save(appointment);
				
				// Send an email to the patient
		        String emailSubject = "Appointment Confirmation";
		        String emailBody = "Dear " + appointment.getPatient().getName() + ",\n\n"
		                + "We are pleased to confirm your appointment with us.\n\n"
		                + "📅 Appointment Details:\n"
		                + "Date & Time: " + appointment.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")) + "\n"
		                + "Doctor: Dr. " + appointment.getDoctor().getUser().getName() + "\n"
		                + "Department: " + appointment.getDoctor().getDepartment().getName() + "\n\n"
		                + "Please arrive at least 15 minutes early and bring any relevant documents or medical records.\n\n"
		                + "If you need to reschedule or have any questions, feel free to contact our support team.\n\n"
		                + "Warm regards,\n"
		                + "Hospital Management Team";

		        emailService.sendEmail(appointment.getPatient().getEmail(), emailSubject, emailBody);

				return new ApiResponse(true, "Already sent email to the patient about confirmation appointment!");
			}else {
				appointment.setConfirmed(request.getConfirmed());
				appointment.setCancelled(true);
				
				appointmentRepository.save(appointment);
				
				// Send a cancellation or denial email to the patient
				String emailSubject = "Appointment Cancellation Notice";

				String emailBody = "Dear " + appointment.getPatient().getName() + ",\n\n"
				        + "We regret to inform you that your appointment scheduled for "
				        + appointment.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")) + " has been cancelled.\n\n"
				        + "❌ Reason: Unfortunately, the appointment has been denied or could not be confirmed due to unforeseen circumstances.\n\n"
				        + "We understand this may be disappointing and apologize for any inconvenience caused. You may reschedule your appointment by contacting our office or using the online booking system.\n\n"
				        + "If you have any questions or need assistance, please don’t hesitate to reach out to our support team.\n\n"
				        + "Thank you for your understanding.\n\n"
				        + "Sincerely,\n"
				        + "Hospital Management Team";


		        emailService.sendEmail(appointment.getPatient().getEmail(), emailSubject, emailBody);

				return new ApiResponse(true, "Already sent email to the patient about cancellation appointment!");
			}
		}
		return new ApiResponse(false, "Cannot do this process!");
	}

	@Override
	public List<AppointmentResposne> getAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		List<AppointmentResposne> responses = new ArrayList<>();
		for(Appointment appointment : appointments) {
			AppointmentResposne response = new AppointmentResposne();
			response.setId(appointment.getId());
			response.setDoctorName(appointment.getDoctor().getUser().getName());
			response.setPatientName(appointment.getPatient().getName());
			response.setDateTime(appointment.getDateTime().toString());
			response.setCancelled(appointment.isCancelled());
			response.setConfirmed(appointment.isConfirmed());
			
			responses.add(response);
		}
		return responses;
	}

	@Override
	public ApiResponse cancelAppointmentByPatient(long id, UserPrincipal currentUser) {
		Appointment appointment = appointmentRepository.findById(id)
	            .orElseThrow(() -> new BadRequestException("Appointment not found"));

	    if (!appointment.getPatient().getId().equals(currentUser.getId())) {
	        throw new UnauthorizedException("You can only cancel your own appointments.");
	    }

	    LocalDateTime appointmentTime = appointment.getDateTime();
	    LocalDateTime now = LocalDateTime.now();

	    if (appointmentTime.minusDays(1).isBefore(now)) {
	        return new ApiResponse(false, "Appointment can only be cancelled at least 1 day in advance.");
	    }

	    appointment.setCancelled(true);
	    appointment.setConfirmed(false);
	    appointmentRepository.save(appointment);

	    // Send cancellation email
	    String emailSubject = "Appointment Cancelled";
	    String emailBody = "Dear " + appointment.getPatient().getName() + ",\n\n"
	            + "Your appointment on " + appointment.getDateTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a"))
	            + " has been successfully cancelled.\n\n"
	            + "If you wish to reschedule, please log in and book a new appointment.\n\n"
	            + "Regards,\nHospital Management Team";

	    emailService.sendEmail(appointment.getPatient().getEmail(), emailSubject, emailBody);

	    return new ApiResponse(true, "Appointment cancelled successfully.");
	}

}

