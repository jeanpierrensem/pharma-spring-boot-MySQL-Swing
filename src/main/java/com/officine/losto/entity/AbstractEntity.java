package com.officine.losto.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.*;
import org.springframework.context.annotation.*;

import java.io.*;

/**
 * The {@code AbstractEntity} class is the abstract class used to keep some common values used by most of the classes.
 * by designing this way, we avoid repeating in subclass id, version parameters, considering that all entities will
 * have at least and id and version
 *
 * <p> @MappedSuperclass : Designates a class whose mapping information is applied to the entities that inherit from it. A mapped
 * superclass has no separate table defined for it.
 * <p> this class did not provide any method
 *
 * @author  JP NSEM
 * @since   1.0
 */

@Description(value = "This bean is designed to be heritated by all bean and serve id and  version properties ")
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Data
public sealed abstract class AbstractEntity implements Serializable
		permits AffectationVendeur, AppGroup, AppUser, AuthAuditLog, Batch, BonCommandeInterne, Category, DrugType, Form, LigneBonCommandeInterne, MagasinCentral, Menu, MouvementStock, Orders, OrdersDetails, Packaging, PointDeVente, Product, Provider, ReceiptDetails, RefreshToken, Section, Sell, SellDetails, Site, SnapshotIndicateur, StockCentral, StockPdv, Threshold

{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private  Long id;

}