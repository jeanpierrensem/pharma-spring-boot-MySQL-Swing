package com.officine.losto.s1.organisation.service;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.s1.organisation.dto.MagasinCentralRequestDto;

import java.util.List;
import java.util.Optional;

public interface MagasinCentralService {

	List<MagasinCentral> findAll();

	MagasinCentral getById(long id);

	Optional<MagasinCentral> findBySiteId(long siteId);

	MagasinCentral create(MagasinCentralRequestDto dto);

	MagasinCentral update(MagasinCentralRequestDto dto);

	void delete(long id);
}
