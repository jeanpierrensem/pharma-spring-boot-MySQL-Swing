package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.s1.organisation.dto.PointDeVenteRequestDto;

import java.util.List;

public interface PointDeVenteService {

	List<PointDeVente> findAll();

	PointDeVente getById(long id);

	List<PointDeVente> findBySiteId(long siteId);

	PointDeVente create(PointDeVenteRequestDto dto);

	PointDeVente update(PointDeVenteRequestDto dto);

	void delete(long id);

	void setActif(long id, boolean actif);
}
