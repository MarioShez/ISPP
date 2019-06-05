
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageService;
import services.MessagesThreadService;
import services.RouteService;
import domain.Actor;
import domain.Administrator;
import domain.Driver;
import domain.Message;
import domain.MessagesThread;
import domain.Passenger;
import domain.Reservation;
import domain.ReservationStatus;
import domain.Route;
import forms.MessageForm;
import forms.ThreadForm;

@Controller
@RequestMapping("/thread")
public class MessagesThreadController extends AbstractController {

	// Services ---------------------------------
	@Autowired
	private MessagesThreadService	mtService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private RouteService			routeService;


	// Constructor ------------------------------
	public MessagesThreadController() {
		super();
	}

	// Messages ---------------------------------

	@RequestMapping(value = "/message/list", method = RequestMethod.GET)
	public ModelAndView messageThreadList() {
		final Actor user = this.actorService.findByPrincipal();
		final Collection<MessagesThread> threads = this.mtService.findMessagesThreadFromParticipant(user.getId());
		final ModelAndView result = new ModelAndView("thread/message/list");
		result.addObject("threads", threads);
		result.addObject("connectedUser", user);
		result.addObject("isReport", false);
		return result;
	}

	@RequestMapping(value = "/message/view", method = RequestMethod.GET)
	public ModelAndView messageThreadView(@RequestParam final int threadId) {
		Assert.notNull(threadId);

		final Actor sender = this.actorService.findByPrincipal();

		ModelAndView result;
		final MessagesThread thread = this.mtService.findOne(threadId);
		if (thread != null && (thread.getParticipantA().getId() == sender.getId() || thread.getParticipantB().getId() == sender.getId()))
			result = this.addMessageThreadModelAndView(this.messageService.construct(thread), false);
		else
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		return result;
	}

	@RequestMapping(value = "/message/create", method = RequestMethod.GET)
	public ModelAndView messageCreate(@RequestParam final int userId, @RequestParam final int routeId) {
		Assert.notNull(userId);
		Assert.notNull(routeId);

		final Route route = this.routeService.findOne(routeId);
		final Actor sender = this.actorService.findByPrincipal();
		final Actor receiver = this.actorService.findOne(userId);

		ModelAndView result;
		if (receiver == null || route == null || receiver.getId() == sender.getId())
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		else {
			final MessagesThread thread = this.mtService.findMessagesThreadFromParticipantsAndRoute(route.getId(), sender.getId(), receiver.getId());
			if (thread == null)
				// Redirigir a la vista de creación
				result = this.createThreadModelAndView(this.mtService.construct(route, receiver), false);
			else
				// Existe ya una conversación, redirigir al hilo en sí
				result = this.addMessageThreadModelAndView(this.messageService.construct(thread), false);
		}
		return result;
	}

	@RequestMapping(value = "/message/create", method = RequestMethod.POST)
	public ModelAndView messageThreadCreate(@ModelAttribute(value = "threadForm") @Valid final ThreadForm threadForm, final BindingResult binding) {
		ModelAndView result = null;
		if (binding.hasErrors())
			result = this.createThreadModelAndView(threadForm, false);
		else {
			final Actor sender = this.actorService.findByPrincipal();
			MessagesThread thread = this.mtService.findMessagesThreadFromParticipantsAndRoute(threadForm.getRoute().getId(), sender.getId(), threadForm.getUser().getId());
			if (thread == null && threadForm.getUser().getId() != sender.getId())
				try {
					thread = this.mtService.reconstruct(threadForm, sender, false);
					thread = this.mtService.saveNew(thread, threadForm.getMessage());
					result = this.addMessageThreadModelAndView(this.messageService.construct(thread), false);
				} catch (final Throwable oops) {
					oops.printStackTrace();
					result = this.createThreadModelAndView(threadForm, false, "thread.commit.error");
				}
			else
				// Error, redirigir a 403
				result = new ModelAndView("redirect:/misc/403.do");
		}
		return result;
	}

	@RequestMapping(value = "/message/add", method = RequestMethod.POST)
	public ModelAndView messageAdd(@ModelAttribute(value = "messageForm") @Valid final MessageForm messageForm, final BindingResult binding) {
		ModelAndView result = null;
		if (binding.hasErrors())
			result = this.addMessageThreadModelAndView(messageForm, false);
		else {
			final Actor sender = this.actorService.findByPrincipal();
			try {
				Message message = this.messageService.reconstruct(messageForm, sender);
				message = this.messageService.save(message);
				result = this.addMessageThreadModelAndView(this.messageService.construct(message.getThread()), false);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				// Error, redirigir a 403
				result = new ModelAndView("redirect:/misc/403.do");
			}
		}
		return result;
	}

	// Reports ----------------------------------

	@RequestMapping(value = "/report/list", method = RequestMethod.GET)
	public ModelAndView reportThreadList() {
		final Actor user = this.actorService.findByPrincipal();
		final Collection<MessagesThread> threads = this.mtService.findReportsThreadFromParticipant(user.getId());
		final ModelAndView result = new ModelAndView("thread/report/list");
		result.addObject("threads", threads);
		result.addObject("connectedUser", user);
		result.addObject("isReport", true);
		return result;
	}

	@RequestMapping(value = "/report/view", method = RequestMethod.GET)
	public ModelAndView reportThreadView(@RequestParam final int threadId) {
		Assert.notNull(threadId);

		final Actor sender = this.actorService.findByPrincipal();

		ModelAndView result;
		final MessagesThread thread = this.mtService.findOne(threadId);
		if (thread != null && (thread.getParticipantA().getId() == sender.getId() || thread.getParticipantB().getId() == sender.getId()))
			result = this.addMessageThreadModelAndView(this.messageService.construct(thread), true);
		else
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		return result;
	}

	@RequestMapping(value = "/report/create", method = RequestMethod.GET)
	public ModelAndView reportCreate(@RequestParam final int userId, @RequestParam final int routeId) {
		Assert.notNull(userId);
		Assert.notNull(routeId);

		final Actor reportingUser = this.actorService.findByPrincipal();
		final Actor reportedUser = this.actorService.findOne(userId);
		final Route route = this.routeService.findOne(routeId);
		boolean passengerParticipant = false;

		ModelAndView result;

		// Si el usuario QUE REPORTA es PASAJERO
		if (reportingUser instanceof Passenger) {
			//Asegurarse que esta reportando al conductor de la ruta
			Assert.isTrue(reportedUser.getId() == route.getDriver().getId());
			//Tambien asegurarse que el PASAJERO QUE REPORTA es PARTICIPANTE de la ruta
			for (final Reservation res : route.getReservations())
				if (res.getStatus() == ReservationStatus.ACCEPTED && res.getPassenger().getId() == reportingUser.getId()) {
					passengerParticipant = true;
					break;
				}
			//Si el PASAJERO QUE REPORTA no es PARTICIPANTE devuelve vista de error 403
			if (!passengerParticipant)
				result = new ModelAndView("redirect:/misc/403.do");
			// Si el usuario QUE REPORTA es CONDUCTOR
		} else if (reportingUser instanceof Driver) {
			//Asegurarse que es el conductor de la ruta
			Assert.isTrue(reportingUser.getId() == route.getDriver().getId());
			//Tambien asegurarse que el PASAJERO REPORTADO es PARTICIPANTE de la ruta
			for (final Reservation res : route.getReservations())
				if (res.getStatus() == ReservationStatus.ACCEPTED && res.getPassenger().getId() == reportedUser.getId()) {
					passengerParticipant = true;
					break;
				}
			//Si el PASAJERO REPORTADO no es PARTICIPANTE devuelve vista de error 403
			if (!passengerParticipant)
				result = new ModelAndView("redirect:/misc/403.do");
		}

		if ((reportedUser == null || route == null || reportingUser.getId() == reportedUser.getId()) || (!this.mtService.canReport(reportedUser, route)))
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		else {
			final MessagesThread thread = this.mtService.findReportsThreadFromParticipantsAndRoute(route.getId(), reportingUser.getId(), reportedUser.getId());
			if (thread == null)
				// Redirigir a la vista de creación
				result = this.createThreadModelAndView(this.mtService.construct(route, reportedUser), true);
			else {
				// Existe ya un reporte, redirigir al hilo en sí
				result = null;
				result = this.addMessageThreadModelAndView(this.messageService.construct(thread), true);
			}
		}

		return result;
	}

	@RequestMapping(value = "/report/create", method = RequestMethod.POST)
	public ModelAndView reportThreadCreate(@ModelAttribute(value = "threadForm") @Valid final ThreadForm threadForm, final BindingResult binding) {
		ModelAndView result = null;
		if (binding.hasErrors())
			result = this.createThreadModelAndView(threadForm, false);
		else {
			final Actor reportingUser = this.actorService.findByPrincipal();
			MessagesThread thread = this.mtService.findReportsThreadFromParticipantsAndRoute(threadForm.getRoute().getId(), reportingUser.getId(), threadForm.getUser().getId());
			if (thread == null && threadForm.getUser().getId() != reportingUser.getId())
				try {
					thread = this.mtService.reconstruct(threadForm, reportingUser, true);
					thread = this.mtService.saveNew(thread, threadForm.getMessage());
					result = this.addMessageThreadModelAndView(this.messageService.construct(thread), true);
				} catch (final Throwable oops) {
					oops.printStackTrace();
					result = this.createThreadModelAndView(threadForm, false, "thread.commit.error");
				}
			else
				// Error, redirigir a 403
				result = new ModelAndView("redirect:/misc/403.do");
		}
		return result;
	}

	@RequestMapping(value = "/report/add", method = RequestMethod.POST)
	public ModelAndView reportMessageAdd(@ModelAttribute(value = "messageForm") @Valid final MessageForm messageForm, final BindingResult binding) {
		ModelAndView result = null;
		if (binding.hasErrors())
			result = this.addMessageThreadModelAndView(messageForm, true);
		else if (!messageForm.getThread().getClosed()) {
			final Actor sender = this.actorService.findByPrincipal();
			try {
				Message message = this.messageService.reconstruct(messageForm, sender);
				message = this.messageService.save(message);
				result = this.addMessageThreadModelAndView(this.messageService.construct(message.getThread()), true);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				// Error, redirigir a 403
				result = new ModelAndView("redirect:/misc/403.do");
			}
		} else
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		return result;
	}

	@RequestMapping(value = "/report/close", method = RequestMethod.GET)
	public ModelAndView reportThreadView(@RequestParam final int threadId, @RequestParam final boolean refund) {
		Assert.notNull(threadId);
		Assert.notNull(refund);

		final Actor admin = this.actorService.findByPrincipal();
		ModelAndView result;
		if (admin instanceof Administrator) {
			MessagesThread thread = this.mtService.findOne(threadId);
			if (thread != null && (thread.getParticipantB().getId() == admin.getId() || thread.getParticipantA().getId() == admin.getId()) && !thread.getClosed() && thread.getReportedUser() != null) {
				thread = this.mtService.closeAndSaveReport(thread, refund);
				result = this.addMessageThreadModelAndView(this.messageService.construct(thread), true);
			} else
				// Error, redirigir a 403
				result = new ModelAndView("redirect:/misc/403.do");
		} else
			// Error, redirigir a 403
			result = new ModelAndView("redirect:/misc/403.do");
		return result;
	}

	// Ancillary Methods ---------------------------------------------------------------------

	private ModelAndView createThreadModelAndView(final ThreadForm form, final boolean isReport) {
		return this.createThreadModelAndView(form, isReport, null);
	}

	private ModelAndView createThreadModelAndView(final ThreadForm form, final boolean isReport, final String message) {
		ModelAndView result;
		if (isReport) {
			result = new ModelAndView("thread/report/create");
			result.addObject("requestURI", "thread/report/create.do");
		} else {
			result = new ModelAndView("thread/message/create");
			result.addObject("requestURI", "thread/message/create.do");
		}
		result.addObject("threadForm", form);
		result.addObject("isReport", isReport);
		result.addObject("message", message);
		result.addObject("connectedUser", this.actorService.findByPrincipal());

		return result;
	}

	private ModelAndView addMessageThreadModelAndView(final MessageForm form, final boolean isReport) {
		return this.addMessageThreadModelAndView(form, isReport, null);
	}

	private ModelAndView addMessageThreadModelAndView(final MessageForm form, final boolean isReport, final String message) {
		MessagesThread thread = form.getThread();
		// Cuando se accede a una conversación que tiene nuevos mensajes para el usuario conectado,
		// se tiene que actualizar el número de nuevos mensajes a cero en dicha conversación en el
		// momento que se accede a la misma
		final int newMessages = thread.getNewMessages();
		if (thread.getNewMessages() > 0) {
			final Actor user = this.actorService.findByPrincipal();
			if ((thread.getLastMessage().getFromAtoB() && user.getId() == thread.getParticipantB().getId()) || (!thread.getLastMessage().getFromAtoB() && user.getId() == thread.getParticipantA().getId()))
				thread = this.mtService.updateReadNewMessages(thread);
		}
		ModelAndView result;
		if (isReport) {
			result = new ModelAndView("thread/report/view");
			result.addObject("requestURI", "thread/report/add.do");
		} else {
			result = new ModelAndView("thread/message/view");
			result.addObject("requestURI", "thread/message/add.do");
		}
		result.addObject("thread", thread);
		result.addObject("newMessages", newMessages);
		result.addObject("messageForm", form);
		result.addObject("isReport", isReport);
		result.addObject("message", message);

		return result;
	}

}
