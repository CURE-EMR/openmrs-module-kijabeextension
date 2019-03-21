package org.openmrs.module.kijabeextension.extension;

import org.apache.commons.collections.map.HashedMap;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.module.appointments.model.Appointment;
import org.openmrs.module.appointments.web.extension.AppointmentResponseExtension;
import org.springframework.stereotype.Component;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;

@Component
public class AddlPatientApptInfo implements AppointmentResponseExtension {
    
    private static final String DEFAULT_PATIENT_ATTRIBUTES = "";
    protected final static Log LOG = LogFactory.getLog(AddlPatientApptInfo.class);
    
    public AddlPatientApptInfo() {
    }

    /**
     * *
     * Fetch attributes name from defined openmrs global properties and fetch
     * values based on attribute name
     *
     * @param appointment
     * @return Map of person attributes and there values
     */
    @Override
    public Map<String, String> run(Appointment appointment) {
        Map<String, String> addlInfo = new HashedMap();
        
        Patient patient = appointment.getPatient();
        String property = Context.getAdministrationService().getGlobalProperty("appointment.patient.attributes");
        property = property != null ? property : DEFAULT_PATIENT_ATTRIBUTES;
        
        String[] attributeNames = property.split(",");
        for (String attributeName : attributeNames) {
            PersonAttribute attribute = patient.getAttribute(attributeName);
            if (attribute != null) {
                addlInfo.put(attribute.getAttributeType().getDescription(), attribute.getAttributeType().getFormat().equalsIgnoreCase("org.openmrs.Concept") ? Context.getConceptService().getConcept(attribute.getValue()).getName().getName() : attribute.getValue());
            }
        }
        return addlInfo;
    }
}
