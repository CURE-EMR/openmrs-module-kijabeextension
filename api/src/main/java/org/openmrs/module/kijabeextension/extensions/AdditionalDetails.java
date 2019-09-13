package org.openmrs.module.kijabeextension.extensions;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.web.extension.AppointmentResponseExtension;
import org.springframework.stereotype.Component;

@Component
public class AdditionalDetails implements AppointmentResponseExtension {
	
	@Override
	public Map<String, String> run(Appointment appointment) {
		Map<String, String> additionalInfo = new HashMap<String, String>();
		Patient patient = appointment.getPatient();
		additionalInfo.put("Patient Phone number", getPersonAttributeValue("PERSON_ATTRIBUTE_TYPE_PHONE_NUMBER", patient));
		additionalInfo.put("Village", getVillage(patient));
		additionalInfo.put("Emergency contact name", getPersonAttributeValue("emergencyContactName", patient));
		additionalInfo.put("Emergency contact number", getPersonAttributeValue("emergencyContactNumber", patient));
		additionalInfo.put("Emergency contact relationship", getPersonAttributeValue("emergencyContactRelationship", patient));
		return additionalInfo;
	}
	
	public String getPersonAttributeValue(String personAttributeName, Patient patient) {
		PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByName(personAttributeName);
		PersonAttribute personAttribute = patient.getAttribute(personAttributeType);
		if (personAttribute != null) {
			return patient.getAttribute(personAttributeType).getValue();
		}
		return "";
	}
	
	public String getVillage(Patient patient) {
		String villageName = "";
		if (patient.getPersonAddress() != null) {
			villageName = patient.getPersonAddress().getCityVillage();
		}
		return villageName;
	}
}
