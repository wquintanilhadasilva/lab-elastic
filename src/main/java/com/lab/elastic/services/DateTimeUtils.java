package com.lab.elastic.services;

import static java.time.format.DateTimeFormatter.ofPattern;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

/**
 * Lida com as conversões de data para a Zona desejada
 *
 * @author Wedson Silva
 * @see <a href="https://dev.azure.com/oobj-devops/Engineering/_workitems/edit/1973">Azzure #1973</a>
 * @since 30/08/2023
 */
public enum DateTimeUtils {
	AMERICA_SAO_PAULO("America/Sao_Paulo");
	
	private final ZoneId zoneId;
	private final DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	DateTimeUtils(String zoneId) {
		Objects.requireNonNull(zoneId, "zoneId não pode ser nulo!");
		this.zoneId = ZoneId.of(zoneId);
	}
	
	public String format(LocalDateTime localDateTime) {
		return Optional.ofNullable(localDateTime).map(DateTimeFormatter.ISO_DATE_TIME::format).orElse(null);
	}
	
	public LocalDateTime now() {
		return ZonedDateTime.now(zoneId).toLocalDateTime();
	}

	public ZonedDateTime nowZoneId() {
		return ZonedDateTime.now(zoneId);
	}
	
	public ZonedDateTime from(String dateTimeString) {
		return LocalDateTime.parse(dateTimeString).atZone(zoneId);
	}
	
	public ZonedDateTime from(String dateTimeString, String pattern) {
		DateTimeFormatter formatter = ofPattern(pattern);
		return LocalDateTime.parse(dateTimeString, formatter).atZone(zoneId);
	}
	
	public ZonedDateTime from(LocalDateTime localDateTime) {
		return ZonedDateTime.of(localDateTime, zoneId);
	}
	
}
