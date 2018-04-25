package com.someairlines.db.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.someairlines.db.FlightRepository;
import com.someairlines.entity.Flight;

@Transactional(isolation = Isolation.READ_COMMITTED)
@Repository
public class HibernaeFlightRepository implements FlightRepository {

	private SessionFactory sessionFactory;
	
	public HibernaeFlightRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Flight> findAll() {
		CriteriaBuilder builder = currentSession().getCriteriaBuilder();
		CriteriaQuery<Flight> criteria = builder.createQuery(Flight.class);
		Root<Flight> rootFlight = criteria.from(Flight.class);
        criteria.select(rootFlight);
        List<Flight> flights = currentSession()
        		.createQuery(criteria)
        		.setHint("org.hibernate.cacheable", true)
        		.getResultList();
		return flights;
	}

	@Override
	@Transactional(readOnly = true)
	public Flight find(final long id) {
		return currentSession().get(Flight.class, id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Flight findAndInitialize(final long id) {
		CriteriaBuilder cb = currentSession().getCriteriaBuilder();
		CriteriaQuery<Flight> q = cb.createQuery(Flight.class);
		Root<Flight> f = q.from(Flight.class);
		f.fetch("flightCrew", JoinType.LEFT);
		q.select(f);
		q.where(cb.equal(f.get("id"), id));
		Flight flight = (Flight) currentSession().createQuery(q).getSingleResult();
		return flight;
	}

	@Override
	public void delete(Flight flight) {
		currentSession().delete(flight);
	}

	@Override
	public void save(Flight flight) {
		currentSession().persist(flight);
	}
	
	@Override
	public void update(final Flight flight) {
		currentSession().update(flight);
	}
	
	@Override
	public void deleteCrew(Flight flight) {
		currentSession().delete(flight.getFlightCrew());
	}
	
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

}
