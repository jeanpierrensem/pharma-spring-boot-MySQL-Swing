package com.officine.losto.s1.organisation.mapper;

import com.officine.losto.entity.MagasinCentral;
import com.officine.losto.entity.PointDeVente;
import com.officine.losto.entity.Site;
import com.officine.losto.s1.organisation.dto.MagasinCentralRequestDto;
import com.officine.losto.s1.organisation.dto.MagasinCentralResponseDto;
import com.officine.losto.s1.organisation.dto.PointDeVenteRequestDto;
import com.officine.losto.s1.organisation.dto.PointDeVenteResponseDto;
import com.officine.losto.s1.organisation.dto.SiteRequestDto;
import com.officine.losto.s1.organisation.dto.SiteResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrganisationMapper {

	public Site toNewSite(SiteRequestDto dto) {
		boolean actif = dto.getActif() == null || dto.getActif();
		return Site.builder()
				.code(dto.getCode())
				.libelle(dto.getLibelle())
				.responsableUserId(dto.getResponsableUserId())
				.actif(actif)
				.build();
	}

	public void updateSiteFromDto(SiteRequestDto dto, Site entity) {
		if (dto.getCode() != null) {
			entity.setCode(dto.getCode());
		}
		if (dto.getLibelle() != null) {
			entity.setLibelle(dto.getLibelle());
		}
		entity.setResponsableUserId(dto.getResponsableUserId());
		if (dto.getActif() != null) {
			entity.setActif(dto.getActif());
		}
	}

	public SiteResponseDto toSiteResponse(Site site, Long magasinCentralId) {
		if (site == null) {
			return null;
		}
		return SiteResponseDto.builder()
				.id(site.getId())
				.code(site.getCode())
				.libelle(site.getLibelle())
				.responsableUserId(site.getResponsableUserId())
				.actif(site.isActif())
				.magasinCentralId(magasinCentralId)
				.build();
	}

	public MagasinCentralResponseDto toMagasinResponse(MagasinCentral m) {
		if (m == null) {
			return null;
		}
		return MagasinCentralResponseDto.builder()
				.id(m.getId())
				.siteId(m.getSite() != null ? m.getSite().getId() : null)
				.code(m.getCode())
				.libelle(m.getLibelle())
				.build();
	}

	public void updateMagasinFromDto(MagasinCentralRequestDto dto, MagasinCentral entity) {
		if (dto.getCode() != null) {
			entity.setCode(dto.getCode());
		}
		if (dto.getLibelle() != null) {
			entity.setLibelle(dto.getLibelle());
		}
	}

	public PointDeVenteResponseDto toPdvResponse(PointDeVente p) {
		if (p == null) {
			return null;
		}
		return PointDeVenteResponseDto.builder()
				.id(p.getId())
				.siteId(p.getSite() != null ? p.getSite().getId() : null)
				.code(p.getCode())
				.libelle(p.getLibelle())
				.adresse(p.getAdresse())
				.phone(p.getPhone())
				.actif(p.isActif())
				.build();
	}

	public void updatePdvFromDto(PointDeVenteRequestDto dto, PointDeVente entity) {
		if (dto.getCode() != null) {
			entity.setCode(dto.getCode());
		}
		if (dto.getLibelle() != null) {
			entity.setLibelle(dto.getLibelle());
		}
		if (dto.getAdresse() != null) {
			entity.setAdresse(dto.getAdresse());
		}
		if (dto.getPhone() != null) {
			entity.setPhone(dto.getPhone());
		}
		if (dto.getActif() != null) {
			entity.setActif(dto.getActif());
		}
	}
}
