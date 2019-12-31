/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.kijabeextension.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This act as a workaround for Friday visit that are closed by default and yet some of them has
 * request for admission that will be responded on Sunday, it set emrapi.visitExpireHours in GP to
 * close visit that have been open since 72 hours on Friday and set 0 to run as normal on Sunday.
 */
@Component
public class AlterGlobalProperty extends AbstractTask {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("EEEE");
	
	public enum weekDay {
		FRIDAY,
		SUNDAY
	}
	
	@Override
	public void execute() {
		Date now = new Date();
		GlobalProperty gb = Context.getAdministrationService().getGlobalPropertyObject("emrapi.visitExpireHours");
		if (isTodayFriday(now)) {
			gb.setPropertyValue("72");
		} else if (isTodaySunday(now)) {
			gb.setPropertyValue("0");
		}
	}
	
	public boolean isTodayFriday(Date today) {
		return SIMPLEDATEFORMAT.format(today).equalsIgnoreCase(weekDay.FRIDAY.toString());
	}
	
	public boolean isTodaySunday(Date today) {
		return SIMPLEDATEFORMAT.format(today).equalsIgnoreCase(weekDay.SUNDAY.toString());
	}
	
}
