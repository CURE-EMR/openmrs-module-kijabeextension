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
		additionalInfo.put("Patient Type", getPersonAttributeValue("patientType", patient));
		additionalInfo.put("Clinic Location", getPersonAttributeValue("clinicLocation", patient));
		additionalInfo.put("First Next Of Kin Phone", getPersonAttributeValue("firstNextOfKinPhone", patient));
		additionalInfo.put("Second Next Of Kin Phone", getPersonAttributeValue("secondNextOfKinPhone", patient));
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
