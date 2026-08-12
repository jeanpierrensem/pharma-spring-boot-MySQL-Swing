package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteResponseDto;

import java.util.List;

public interface SiteService {

	List<Site> findAll();

	Site getById(long id);

	SiteResponseDto buildResponse(Site site);

	Site create(SiteRequestDto dto);

	Site update(SiteRequestDto dto);

	void delete(long id);

	void setActif(long id, boolean actif);

	List<PointDeVente> findPointsDeVenteBySite(long siteId);
}
