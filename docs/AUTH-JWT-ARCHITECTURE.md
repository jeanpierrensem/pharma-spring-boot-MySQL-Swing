# Authentification JWT — Officine (Spring Boot + JavaFX)

Architecture stateless JWT pour le backend Spring Boot 3.4 et le client bureau JavaFX 21.

> **Port API** : `9005` (configurable via `server.port`). Le client JavaFX pointe par défaut sur `http://localhost:9005/api/`.

---

## Structure des packages (backend)

```
com.officine.losto
├── config/
│   ├── SecurityConfig.java          # Spring Security 6, JWT filter, rôles
│   ├── JwtProperties.java           # officine.jwt.*
│   └── CorsConfig.java              # CORS clients distants / JavaFX
├── security/
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── CustomUserDetailsService.java
│   ├── OfficineUserDetails.java     # UserDetails + mapping rôles
│   └── LoginRateLimiter.java
├── controller/
│   ├── AuthenticationController.java
│   ├── AdminController.java         # /api/admin/**
│   └── UserAreaController.java      # /api/user/**
├── service/
│   ├── AuthService.java
│   └── AuthServiceImpl.java
├── dto/auth/
│   ├── LoginRequestDto.java
│   ├── RegisterRequestDto.java
│   ├── JwtAuthResponseDto.java
│   ├── RefreshTokenRequestDto.java
│   └── CurrentUserResponseDto.java
├── entity/
│   ├── AppUser.java                 # + enabled, createdAt
│   ├── RefreshToken.java
│   └── AuthAuditLog.java
└── validation/
    ├── StrongPassword.java
    └── StrongPasswordValidator.java
```

## Structure client JavaFX

```
officine-client/
├── pom.xml
└── src/main/java/com/officine/client/
    ├── OfficineClientApp.java
    ├── config/ClientConfig.java
    ├── api/ApiClient.java
    ├── auth/AuthService.java, SessionManager.java, TokenStorage.java, RouteGuard.java
    └── ui/login|register|dashboard/*Controller.java
```

---

## Diagramme d'architecture

```mermaid
flowchart TB
    subgraph Desktop["Client JavaFX"]
        UI[Login / Register / Dashboard]
        AuthSvc[AuthService]
        Session[SessionManager]
        Api[ApiClient + Bearer Token]
        UI --> AuthSvc --> Api
        AuthSvc --> Session
    end

    subgraph Backend["Spring Boot API :9005"]
        AuthCtrl[AuthenticationController]
        Filter[JwtAuthenticationFilter]
        Sec[SecurityConfig]
        Jwt[JwtService]
        UserDetails[CustomUserDetailsService]
        DB[(H2 / MySQL)]
        AuthCtrl --> Jwt
        Filter --> Jwt
        Filter --> UserDetails
        UserDetails --> DB
        Sec --> Filter
    end

    Api -->|REST JSON| AuthCtrl
    Api -->|REST JSON + JWT| Filter
```

---

## Flow JWT (stateless)

```mermaid
sequenceDiagram
    participant C as Client JavaFX
    participant A as /api/auth/login
    participant S as AuthService
    participant J as JwtService
    participant F as JwtAuthenticationFilter

    C->>A: POST login (username, password)
    A->>S: authenticate + audit
    S->>J: generateAccessToken()
    S-->>C: accessToken + refreshToken + roles + expiration
    C->>F: GET /api/users/me + Authorization Bearer
    F->>J: validate token
    F-->>C: 200 CurrentUser
```

---

## Séquence login / logout

```mermaid
sequenceDiagram
    participant U as Utilisateur
    participant FX as JavaFX
    participant API as Backend

    U->>FX: Saisie login/password
    FX->>API: POST /api/auth/login
    API-->>FX: JWT + refreshToken
    FX->>FX: SessionManager + TokenStorage (Remember Me)
    FX->>API: GET /api/users/me
    API-->>FX: profil + rôles
    FX->>FX: Dashboard

    U->>FX: Déconnexion
    FX->>API: POST /api/auth/logout + refreshToken
    API-->>FX: 204
    FX->>FX: clear session
```

---

## Endpoints

| Méthode | URL | Accès | Description |
|---------|-----|-------|-------------|
| POST | `/api/auth/register` | Public | Inscription USER |
| POST | `/api/auth/login` | Public | Login + JWT |
| POST | `/api/auth/refresh` | Public | Renouvellement via refresh token |
| POST | `/api/auth/logout` | Authentifié | Révoque refresh token |
| GET | `/api/users/me` | Authentifié | Profil courant |
| GET | `/api/admin/**` | ROLE_ADMIN | Zone admin |
| GET | `/api/user/**` | USER ou ADMIN | Zone utilisateur |
| `/**` (autres API) | Authentifié | Bearer JWT requis |

---

## Exemples HTTP

### Login

```http
POST http://localhost:9005/api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "secret123",
  "rememberMe": true
}
```

**Réponse 200**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "tokenType": "Bearer",
  "username": "admin",
  "roles": ["ADMIN"],
  "expiration": "2026-05-23T15:30:00Z"
}
```

### Register

```http
POST http://localhost:9005/api/auth/register
Content-Type: application/json

{
  "username": "jdupont",
  "name": "Jean Dupont",
  "email": "jean@example.com",
  "password": "Secret123!"
}
```

### Profil courant

```http
GET http://localhost:9005/api/users/me
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Réponse 200**

```json
{
  "id": 1,
  "login": "admin",
  "name": "Admin User",
  "email": "admin@officine.dev",
  "enabled": true,
  "createdAt": "2026-05-23T14:00:00Z",
  "group": { "id": 1, "code": "Administrators", "label": "Gestion complète" },
  "roles": ["ADMIN"]
}
```

### Erreur validation (400)

```json
{
  "status": 400,
  "error": "Validation failed",
  "message": "Request body failed validation",
  "fieldErrors": [
    { "field": "password", "message": "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre", "rejected": "weak" }
  ]
}
```

---

## Mapping rôles (APP_GROUP → Spring Security)

| Groupe (`APP_GROUP.NAME`) | Rôle Spring |
|---------------------------|-------------|
| Administrators, *ADMIN*   | `ROLE_ADMIN` |
| Autres (USER, Pharmaciens, Consultants…) | `ROLE_USER` |

---

## Configuration (`application.properties`)

```properties
officine.jwt.secret=${OFFICINE_JWT_SECRET:...}
officine.jwt.access-token-expiration-ms=900000
officine.jwt.refresh-token-expiration-ms=604800000
officine.jwt.remember-me-refresh-token-expiration-ms=2592000000
officine.jwt.issuer=officine-api
officine.cors.allowed-origins=*
```

---

## Comptes de démo (profil `dev`)

| Login | Mot de passe | Rôle |
|-------|--------------|------|
| admin | secret123 | ADMIN |
| pharma | secret123 | USER |
| consult | secret123 | USER |

---

## Lancer l'application

**Backend**

```bash
./mvnw spring-boot:run
```

**Client JavaFX**

```bash
cd officine-client
mvn javafx:run
```

Variable d'environnement optionnelle : `OFFICINE_API_BASE_URL=http://localhost:9005/api/`

---

## Sécurité implémentée

- BCrypt (`PasswordEncoder`)
- JWT stateless (access + refresh)
- Rate limiting login (5 tentatives / 5 min)
- Audit logs (`AUTH_AUDIT_LOG`)
- Validation mot de passe fort + email
- `@PreAuthorize` sur zones admin/user
- Migration automatique mots de passe legacy en clair → BCrypt au login
- Remember Me (refresh token longue durée)
- Route guards JavaFX (`RouteGuard`)

---

## Bonnes pratiques production

1. Remplacer `officine.jwt.secret` par une variable d'environnement forte (256 bits+).
2. Activer HTTPS en production.
3. Restreindre `officine.cors.allowed-origins`.
4. Passer les mots de passe dev en BCrypt uniquement (déjà fait au seed).
5. Ajouter `spring-security-test` pour tests d'intégration auth.
