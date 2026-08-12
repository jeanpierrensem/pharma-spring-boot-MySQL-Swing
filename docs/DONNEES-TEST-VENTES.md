# Données de test — Ventes, bons internes, planning vendeur

Ces enregistrements sont insérés automatiquement en **profil Spring `dev`** par :

- `OrganisationDemoDataSeeder` (`@Order(200)`) : sites, magasins centraux, points de vente
- `VentesS5DemoDataSeeder` (`@Order(300)`) : `SELL`, `SELL_DETAILS`, `BON_COMMANDE_INTERNE`, `LIGNE_BON_COMMANDE_INTERNE`, `AFFECTATION_VENDEUR`

**Lancement** : base vide, puis `mvn spring-boot:run` avec `--spring.profiles.active=dev` (ou `application-dev.properties`).

**Mot de passe** des comptes seedés : `secret123` (défini dans `OfficineApplication`).

---

## Comptes applicatifs (pour filtre “vendeur / caissier”)

| Login   | Rôle        | Utilisation en démo                          |
|--------|-------------|-----------------------------------------------|
| `pharma` | Pharmaciens | Vendeur enregistreur sur les ventes et bons  |
| `admin`  | Administrateurs | 2e créneau de planning (caisse Paris)  |

---

## Organisation (codes techniques)

| Type            | Code                    | Libellé (indicatif)        |
|-----------------|-------------------------|----------------------------|
| Site            | `DEMO-PARIS`            | Pharmacie démo — Paris     |
| Site            | `DEMO-LYON`             | Pharmacie démo — Lyon      |
| Magasin central | `DEMO-MC-PARIS`         | (lié au site Paris)        |
| Magasin central | `DEMO-MC-LYON`          | (lié au site Lyon)         |
| PDV             | `DEMO-PDV-PARIS-CAISSE` | Caisse principale (Paris)  |
| PDV             | `DEMO-PDV-PARIS-ORTHO`  | Espace orthopédie (Paris)  |
| PDV             | `DEMO-PDV-LYON-CAISSE`  | Caisse hall A (Lyon)       |

**Client JavaFX (filtres)** : choisir site `DEMO-PARIS` / PDV contenant *Caisse principale* / utilisateur *pharma* / plage 2026-04-01 → 2026-04-10 pour voir les tickets de démo.

---

## Produits (code-barres du seed `populate()`)

| Code-barres   | Produit (indicatif)   |
|---------------|------------------------|
| `3770012345678` | Paracétamol 500 mg   |
| `3770012345685` | Ibuprofène 400 mg     |
| `3770099990001` | Vitamine D3 1000 UI   |

---

## Ventes (tickets) — `SELL` / `SELL_DETAILS`

| N° ticket     | Date vente | Total TTC | Client        | Paiement | Site     | PDV (code)            | Vendeur (user) |
|---------------|------------|-----------|---------------|----------|----------|------------------------|----------------|
| `VTE-2026-0001` | 2026-04-01 | 23,40 €   | Client comptoir | Espèces | DEMO-PARIS | DEMO-PDV-PARIS-CAISSE | pharma (Marie) |
| `VTE-2026-0002` | 2026-04-02 | 15,00 €   | Dr Martin     | Carte    | DEMO-PARIS | DEMO-PDV-PARIS-CAISSE | pharma         |

**Lignes** :

- VTE-2026-0001 : paracétamol ×2 (prix ligne 7,80), vitamine D3 ×1 remise 5 % (11,40)
- VTE-2026-0002 : ibuprofène ×2 (7,50 / unité sur la ligne, total 15,00)

**API (exemples)** :

- `GET /api/sells` — toutes les ventes (avec graph + lignes)
- `GET /api/sells/filter?siteId=<id>&pointDeVenteId=<id>&from=2026-04-01&to=2026-04-30&effectueeParUserId=<id>`
- `GET /api/sells/by-code?code=VTE-2026-0001`

---

## Bons internes — `BON_COMMANDE_INTERNE` / `LIGNE_BON_COMMANDE_INTERNE`

| N° bon              | Date commande | Statut   | Site      | PDV (code)            | User  |
|---------------------|---------------|----------|-----------|------------------------|-------|
| `BINT-DEMO-2026-001` | 2026-04-03   | `SOUMIS` | DEMO-PARIS | DEMO-PDV-PARIS-CAISSE | pharma |
| `BINT-DEMO-2026-002` | 2026-04-04   | `EN_COURS` | DEMO-LYON | DEMO-PDV-LYON-CAISSE  | pharma |

Lignes (résumé) :

- 001 : paracétamol ×24 @ 2,15 € ; ibuprofène ×10 @ 4,20 €
- 002 : vitamine D3 ×18 @ 6,80 € (seulement si le seed Lyon a tourné)

**API** :

- `GET /api/bons-commande-interne`
- `GET /api/bons-commande-interne/filter?siteId=…&from=2026-04-01&to=2026-04-30`

---

## Planning vendeur — `AFFECTATION_VENDEUR`

| Créneau (début → fin)     | Actif plage | Utilisateur | PDV (code)            |
|----------------------------|-------------|------------|------------------------|
| 2026-04-01 08:00 → 12:30 | oui         | pharma     | DEMO-PDV-PARIS-CAISSE  |
| 2026-04-02 14:00 → 18:00 | oui         | admin      | DEMO-PDV-PARIS-CAISSE  |
| 2026-04-05 09:00 → 13:00 | non         | pharma     | DEMO-PDV-PARIS-ORTHO   |

**API** :

- `GET /api/affectations-vendeur`
- `GET /api/affectations-vendeur/filter?pointDeVenteId=…&appUserId=…`

---

## Recharger le jeu de démo

Si `VTE-2026-0001` ou `BINT-DEMO-2026-001` existent déjà, le seeder ne duplique pas. Pour repartir de zéro : base vide, ou supprimer manuellement les lignes des tables concernées puis redémarrer en `dev`.
