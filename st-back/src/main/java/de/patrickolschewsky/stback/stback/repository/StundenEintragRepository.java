package de.patrickolschewsky.stback.stback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.patrickolschewsky.stback.stback.model.StundenEintrag;


@Repository
public interface StundenEintragRepository extends JpaRepository<StundenEintrag, Long> {

	@Query("select s from StundenEintrag s where s.benutzer.name = ?1 "
			+ "and YEAR(s.datum) = ?2 and MONTH(s.datum) = ?3")
	List<StundenEintrag> findByBenutzerJahrMonat(String benutzer, Integer jahr, Integer monat);

}
