/**
 * Sous-système ventes (tickets, lignes, périmètre site / point de caisse / vendeur).
 * <p>
 * Intégration stock (phase 2) : sur validation d’une vente, prévoir un mouvement de sortie
 * {@code stock_pdv} et un enregistrement {@code mouvement_stock} avec
 * {@code reference_type} = SELL, {@code reference_id} = id du ticket, pour tracer la
 * sortie des quantités vendues.
 */
package com.officine.losto.s4.ventes;
