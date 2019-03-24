
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Route extends DomainEntity {

	//Attributes

	private Date						departureDate;
	private String						origin;
	private String						destination;
	private Integer						stimatedDuration;
	private String						daysRepeat;
	private Integer						avaliableSeats;
	private Double						distance;
	private Double						pricePerPassenger;
	private String						details;
	private LuggageSize					maxLugagge;

	//Relationships

	private Driver						driver;
	private Collection<ControlPoint>	controlPoints;
	private Collection<Reserve>			reserves;


	//Getter

	@NotBlank
	@Pattern(regexp = "MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY")
	public String getDaysRepeat() {
		return this.daysRepeat;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDepartureDate() {
		return this.departureDate;
	}

	@NotBlank
	public String getOrigin() {
		return this.origin;
	}

	@NotBlank
	public String getDestination() {
		return this.destination;
	}

	@NotNull
	@Min(value = 1)
	public Integer getStimatedDuration() {
		return this.stimatedDuration;
	}

	@Min(value = 0)
	@Digits(fraction = 2, integer = 4)
	public Double getDistance() {
		return this.distance;
	}
	@NotNull
	@Min(value = 1)
	public Integer getAvaliableSeats() {
		return this.avaliableSeats;
	}

	@NotNull
	@Min(value = (long) 1.10)
	public Double getPricePerPassenger() {
		return this.pricePerPassenger;
	}

	@NotBlank
	public String getDetails() {
		return this.details;
	}

	@Valid
	@NotNull
	public LuggageSize getMaxLugagge() {
		return this.maxLugagge;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Driver getDriver() {
		return this.driver;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "route")
	public Collection<ControlPoint> getControlPoints() {
		return this.controlPoints;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "route")
	public Collection<Reserve> getReserves() {
		return this.reserves;
	}

	//Setter

	public void setDepartureDate(final Date departureDate) {
		this.departureDate = departureDate;
	}

	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	public void setDestination(final String destination) {
		this.destination = destination;
	}

	public void setStimatedDuration(final Integer stimatedDuration) {
		this.stimatedDuration = stimatedDuration;
	}

	public void setDaysRepeat(final String daysRepeat) {
		this.daysRepeat = daysRepeat;
	}

	public void setDistance(final Double distance) {
		this.distance = distance;
	}

	public void setDriver(final Driver driver) {
		this.driver = driver;
	}

	public void setControlPoints(final Collection<ControlPoint> controlPoints) {
		this.controlPoints = controlPoints;
	}
	public void setReserves(final Collection<Reserve> reserves) {
		this.reserves = reserves;
	}

	public void setAvaliableSeats(final Integer avaliableSeats) {
		this.avaliableSeats = avaliableSeats;
	}

	public void setPricePerPassenger(final Double pricePerPassenger) {
		this.pricePerPassenger = pricePerPassenger;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public void setMaxLugagge(final LuggageSize maxLugagge) {
		this.maxLugagge = maxLugagge;
	}

}