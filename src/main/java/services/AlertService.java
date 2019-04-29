
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AlertRepository;
import security.UserAccountService;
import domain.Actor;
import domain.Alert;

@Service
@Transactional
public class AlertService {

	//Managed repository

	@Autowired
	private AlertRepository			alertRepository;

	//Supporting services

	@Autowired
	private MessageService			messageService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private DriverService			driverService;

	@Autowired
	private PassengerService		passengerService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		uaService;


	public Alert create() {
		final Alert alert = new Alert();
		final Actor sender = this.actorService.findByPrincipal();
		alert.setSender(sender);
		alert.setIsRead(false);
		alert.setDate(new Date(System.currentTimeMillis() - 1));

		return alert;
	}
	public Collection<Alert> listByActor(final int id) {
		final Collection<Alert> result = new ArrayList<Alert>();

		final Collection<Alert> allAlerts = this.alertRepository.findAll();
		for (final Alert al : allAlerts)
			for (final Actor ac : al.getReceiver())
				if (ac.getId() == id)
					result.add(al);
		return result;
	}

	public Alert findOne(final int id) {
		Assert.isTrue(id > 0);
		return this.alertRepository.findOne(id);
	}

	public Collection<Alert> findAll() {
		return this.alertRepository.findAll();
	}

	public void alertSeen(final Alert alert) {
		Assert.notNull(alert);

		// Comprobar que el usuario conectado es el propietario de la ruta
		//Assert.isTrue(this.actorService.findByPrincipal().getId() == Alert.getDriver().getId());

		alert.setIsRead(true);

		this.alertRepository.save(alert);
	}
	public void delete(final Alert alert) {

		Assert.notNull(alert);

		//Assertion that the user deleting this task has the correct privilege.
		//Assert.isTrue(this.actorService.findByPrincipal().getId() == r.getDriver().getId());

		this.alertRepository.delete(alert);
	}

}