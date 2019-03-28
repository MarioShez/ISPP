
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.ReservationStatus;
import domain.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {

	@Query("select r from Route r join r.reservations res where res.passenger.id=?1 and res.status != ?2 and res.status != ?3 and r.isCancelled = FALSE")
	Collection<Route> findActiveRoutesByPassenger(int passengerId, ReservationStatus statusCancelled, ReservationStatus statusRejected);	//Cuando se llame desde el servicio se le pasar� como parametro el estado de reserva cancelada Y denegada

	@Query("select r from Route r where r.driver.id=?1 and r.departureDate>?2 and r.isCancelled = FALSE ")
	Collection<Route> findActiveRoutesByDriver(int driverId, Date now);	//Cuando se llame desde el servicio se le pasar� como parametro la fecha y hora actual

	@Query(value = "select * from Route where (is_cancelled = false) and (departure_date > now()) and (available_seats >= ?1)", nativeQuery = true)
	Collection<Route> searchRoutes(int availableSeats);

}
