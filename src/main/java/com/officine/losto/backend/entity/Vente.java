package com.officine.losto.backend.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class Vente extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	    private String numero; //1
	    private String ventedate;
	    private String vendeur;
	    private String client; // ou référence à une entité Client
	    private String typeVente; // COMPTANT, LIVRAISON, ORDONNANCE
	    private String modePaiement; //Espèces / Carte / Chèque / Virement / Mobile 
	    private BigDecimal montantTotal;//Espèces / Carte / Chèque / Virement / Mobile 
	    private BigDecimal montantPaye;
	    private BigDecimal montantRendu;
	    private String remarque;

	    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	    private List<VenteLigne> lignes = new ArrayList<>();

/*	@Version
	@Column(name = "VERSION")
	private int version;
*/
	
}
