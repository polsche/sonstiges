package de.patrickolschewsky.stback.stback.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.stereotype.Component;

import de.patrickolschewsky.stback.stback.model.StundenEintrag;

@Component
public class DBAccess {
	
	@Autowired
    private JpaContext jpaContext;
	
	public EntityManager em;
	
	public List<StundenEintrag> findByBenutzerJahrMonat(String benutzer, Integer year, Integer monthValue) {
		EntityManager em = jpaContext.getEntityManagerByManagedType(StundenEintrag.class);
		this.em = em;
		return em.createQuery("select s from StundenEintrag s where s.benutzer.name = :name " +
				"and s.datum.year = :jahr and s.datum.monthValue = :monat").setParameter("name", benutzer)
				.setParameter("jahr", year).setParameter("monat", monthValue)
				.getResultList();
		
	}

}
