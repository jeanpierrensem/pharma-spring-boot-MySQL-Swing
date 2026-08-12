package com.officine.losto.dto.mapper;

import com.officine.losto.dto.*;
import com.officine.losto.dto.auth.*;
import com.officine.losto.entity.*;
import com.officine.losto.model.*;
import com.officine.losto.s1.organisation.repository.*;
import com.officine.losto.s5.reappro.dto.*;
import com.officine.losto.s5.reappro.repository.*;
import com.officine.losto.security.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

/**
 * Maps domain entities to API DTOs (stable JSON) and request DTOs back to entities, resolving FKs via repositories.
 */
@Component
public class DtoMapper {

    private final UserRepo userRepo;
    private final GroupRepo groupRepo;
    private final CategoryRepo categoryRepo;
    private final FormRepo formRepo;
    private final DrugTypeRepo drugTypeRepo;
    private final SectionRepo sectionRepo;
    private final BatchRepo batchRepo;
    private final ProviderRepo providerRepo;
    private final ThresholdRepo thresholdRepo;
    private final PackagingRepo packagingRepo;
    private final ProductRepo productRepo;
    private final MenuRepo menuRepo;
    private final OrdersRepo ordersRepo;
    private final OrderDetailsRepo orderDetailsRepo;
    private final ReceiptDetailsRepo receiptDetailsRepo;
    private final SellRepo sellRepo;
    private final SellDetailsRepo sellDetailsRepo;
    private final SiteRepository siteRepository;
    private final PointDeVenteRepository pointDeVenteRepository;
    private final MagasinCentralRepository magasinCentralRepository;
    private final BonCommandeInterneRepository bonCommandeInterneRepository;
    private final AffectationVendeurRepository affectationVendeurRepository;
    private final PasswordEncoder passwordEncoder;

    public DtoMapper(
            UserRepo userRepo,
            GroupRepo groupRepo,
            CategoryRepo categoryRepo,
            FormRepo formRepo,
            DrugTypeRepo drugTypeRepo,
            SectionRepo sectionRepo,
            BatchRepo batchRepo,
            ProviderRepo providerRepo,
            ThresholdRepo thresholdRepo,
            PackagingRepo packagingRepo,
            ProductRepo productRepo,
            MenuRepo menuRepo,
            OrdersRepo ordersRepo,
            OrderDetailsRepo orderDetailsRepo,
            ReceiptDetailsRepo receiptDetailsRepo,
            SellRepo sellRepo,
            SellDetailsRepo sellDetailsRepo,
            SiteRepository siteRepository,
            PointDeVenteRepository pointDeVenteRepository,
            MagasinCentralRepository magasinCentralRepository,
            BonCommandeInterneRepository bonCommandeInterneRepository,
            AffectationVendeurRepository affectationVendeurRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.categoryRepo = categoryRepo;
        this.formRepo = formRepo;
        this.drugTypeRepo = drugTypeRepo;
        this.sectionRepo = sectionRepo;
        this.batchRepo = batchRepo;
        this.providerRepo = providerRepo;
        this.thresholdRepo = thresholdRepo;
        this.packagingRepo = packagingRepo;
        this.productRepo = productRepo;
        this.menuRepo = menuRepo;
        this.ordersRepo = ordersRepo;
        this.orderDetailsRepo = orderDetailsRepo;
        this.receiptDetailsRepo = receiptDetailsRepo;
        this.sellRepo = sellRepo;
        this.sellDetailsRepo = sellDetailsRepo;
        this.siteRepository = siteRepository;
        this.pointDeVenteRepository = pointDeVenteRepository;
        this.magasinCentralRepository = magasinCentralRepository;
        this.bonCommandeInterneRepository = bonCommandeInterneRepository;
        this.affectationVendeurRepository = affectationVendeurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private static EntityRefDto ref(Long id, String code, String label) {
        if (id == null) {
            return null;
        }
        return EntityRefDto.builder().id(id).code(code).label(label).build();
    }

    public AppUserResponseDto toUserResponse(AppUser u) {
        if (u == null) {
            return null;
        }
        AppGroup g = u.getGroup();
        String photoUrl = (u.getPhotoFilename() != null && !u.getPhotoFilename().isBlank())
                ? ("users/" + u.getId() + "/photo")
                : null;
        return AppUserResponseDto.builder()
                .id(u.getId())
                .login(u.getLogin())
                .phoneNumber(u.getPhoneNumber())
                .name(u.getName())
                .email(u.getEmail())
                .group(g == null ? null : ref(g.getId(), g.getName(), g.getDescription()))
                .profilePhotoUrl(photoUrl)
                .build();
    }

    public CurrentUserResponseDto toCurrentUserResponse(AppUser u) {
        if (u == null) {
            return null;
        }
        AppGroup g = u.getGroup();
        return CurrentUserResponseDto.builder()
                .id(u.getId())
                .login(u.getLogin())
                .name(u.getName())
                .email(u.getEmail())
                .enabled(Boolean.TRUE.equals(u.getEnabled()))
                .createdAt(u.getCreatedAt())
                .group(g == null ? null : ref(g.getId(), g.getName(), g.getDescription()))
                .roles(OfficineUserDetails.RoleAuthorityMapper.roleNamesFor(u))
                .build();
    }

    public AppUser toAppUser(AppUserRequestDto dto) {
        AppUser u = dto.getId() != null
                ? userRepo.findById(dto.getId()).orElseThrow()
                : AppUser.builder().build();
        if (dto.getLogin() != null) {
            u.setLogin(dto.getLogin());
        }
        if (dto.getPhoneNumber() != null) {
            u.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getName() != null) {
            u.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            u.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getGroupId() != null) {
            u.setGroup(groupRepo.findById(dto.getGroupId()).orElse(null));
        }
        return u;
    }

    public AppGroupResponseDto toGroupResponse(AppGroup g) {
        if (g == null) {
            return null;
        }
        List<EntityRefDto> menus = g.getMenus() == null ? Collections.emptyList()
                : g.getMenus().stream()
                .filter(Objects::nonNull)
                .map(m -> ref(m.getId(), m.getName(), m.getDescription()))
                .collect(Collectors.toList());
        return AppGroupResponseDto.builder()
                .id(g.getId())
                .name(g.getName())
                .description(g.getDescription())
                .selected(Boolean.TRUE.equals(g.getSelected()))
                .menus(menus)
                .build();
    }

    public AppGroup toAppGroup(AppGroupRequestDto dto) {
        AppGroup g = dto.getId() != null
                ? groupRepo.findById(dto.getId()).orElseThrow()
                : AppGroup.builder().build();
        if (dto.getName() != null) {
            g.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            g.setDescription(dto.getDescription());
        }
        g.setSelected(dto.isSelected());
        if (dto.getMenuIds() != null) {
            g.getMenus().clear();
            for (Long mid : dto.getMenuIds()) {
                if (mid != null) {
                    menuRepo.findById(mid).ifPresent(g.getMenus()::add);
                }
            }
        }
        return g;
    }

    public CategoryResponseDto toCategoryResponse(Category e) {
        if (e == null) {
            return null;
        }
        return CategoryResponseDto.builder().id(e.getId()).code(e.getCode()).description(e.getDescription()).build();
    }

    public Category toCategory(CategoryRequestDto dto) {
        Category e = dto.getId() != null
                ? categoryRepo.findById(dto.getId()).orElseThrow()
                : Category.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        return e;
    }

    public FormResponseDto toFormResponse(Form e) {
        if (e == null) {
            return null;
        }
        return FormResponseDto.builder().id(e.getId()).code(e.getCode()).description(e.getDescription()).build();
    }

    public Form toForm(FormRequestDto dto) {
        Form e = dto.getId() != null ? formRepo.findById(dto.getId()).orElseThrow() : Form.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        return e;
    }

    public DrugTypeResponseDto toDrugTypeResponse(DrugType e) {
        if (e == null) {
            return null;
        }
        return DrugTypeResponseDto.builder().id(e.getId()).code(e.getCode()).description(e.getDescription()).build();
    }

    public DrugType toDrugType(DrugTypeRequestDto dto) {
        DrugType e = dto.getId() != null ? drugTypeRepo.findById(dto.getId()).orElseThrow() : DrugType.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        return e;
    }

    public MenuResponseDto toMenuResponse(Menu e) {
        if (e == null) {
            return null;
        }
        List<Long> groupIds = e.getGroups() == null ? Collections.emptyList()
                : e.getGroups().stream()
                .filter(Objects::nonNull)
                .map(AppGroup::getId)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
        Menu p = e.getParent();
        return MenuResponseDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .active(Boolean.TRUE.equals(e.getActive()))
                .pathCode(e.getPathCode())
                .parentId(p == null ? null : p.getId())
                .treeLevel(e.getTreeLevel())
                .sortOrder(e.getSortOrder())
                .groupIds(groupIds)
                .build();
    }

    public MenuTreeNodeDto toMenuTreeNode(Menu m) {
        if (m == null) {
            return null;
        }
        List<MenuTreeNodeDto> childDtos = m.getChildren() == null ? Collections.emptyList()
                : m.getChildren().stream()
                .filter(Objects::nonNull)
                .map(this::toMenuTreeNode)
                .collect(Collectors.toList());
        Long id = m.getId();
        return MenuTreeNodeDto.builder()
                .id(id)
                .name(m.getName())
                .description(m.getDescription())
                .pathCode(m.getPathCode())
                .treeLevel(m.getTreeLevel())
                .sortOrder(m.getSortOrder())
                .children(childDtos)
                .build();
    }

    public Menu toMenu(MenuRequestDto dto) {
        Menu e = dto.getId() != null ? menuRepo.findById(dto.getId()).orElseThrow() : Menu.builder().build();
        if (dto.getName() != null) {
            e.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        e.setActive(dto.isActive());
        return e;
    }

    public SectionResponseDto toSectionResponse(Section e) {
        if (e == null) {
            return null;
        }
        return SectionResponseDto.builder().id(e.getId()).code(e.getCode()).description(e.getDescription()).build();
    }

    public Section toSection(SectionRequestDto dto) {
        Section e = dto.getId() != null ? sectionRepo.findById(dto.getId()).orElseThrow() : Section.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        return e;
    }

    public BatchResponseDto toBatchResponse(Batch e) {
        if (e == null) {
            return null;
        }
        Provider p = e.getProvider();
        return BatchResponseDto.builder()
                .id(e.getId())
                .number(e.getNumber())
                .expiredDate(e.getExpiredDate())
                .quantity(e.getQuantity())
                .provider(p == null ? null : ref(p.getId(), p.getCode(), p.getDesignation()))
                .build();
    }

    public Batch toBatch(BatchRequestDto dto) {
        Batch e = dto.getId() != null ? batchRepo.findById(dto.getId()).orElseThrow() : Batch.builder().build();
        if (dto.getNumber() != null) {
            e.setNumber(dto.getNumber());
        }
        if (dto.getExpiredDate() != null) {
            e.setExpiredDate(dto.getExpiredDate());
        }
        e.setQuantity(dto.getQuantity());
        if (dto.getProviderId() != null) {
            e.setProvider(providerRepo.findById(dto.getProviderId()).orElse(null));
        }
        return e;
    }

    public ProviderResponseDto toProviderResponse(Provider e) {
        if (e == null) {
            return null;
        }
        return ProviderResponseDto.builder()
                .id(e.getId())
                .code(e.getCode())
                .designation(e.getDesignation())
                .address(e.getAddress())
                .phoneNumber(e.getPhoneNumber())
                .email(e.getEmail())
                .build();
    }

    public Provider toProvider(ProviderRequestDto dto) {
        Provider e = dto.getId() != null ? providerRepo.findById(dto.getId()).orElseThrow() : Provider.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDesignation() != null) {
            e.setDesignation(dto.getDesignation());
        }
        if (dto.getAddress() != null) {
            e.setAddress(dto.getAddress());
        }
        if (dto.getPhoneNumber() != null) {
            e.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getEmail() != null) {
            e.setEmail(dto.getEmail());
        }
        return e;
    }

    public ThresholdResponseDto toThresholdResponse(Threshold e) {
        if (e == null) {
            return null;
        }
        return ThresholdResponseDto.builder()
                .id(e.getId())
                .code(e.getCode())
                .level(e.getLevel())
                .description(e.getDescription())
                .colorHex(e.getColorHex())
                .build();
    }

    public Threshold toThreshold(ThresholdRequestDto dto) {
        Threshold e = dto.getId() != null ? thresholdRepo.findById(dto.getId()).orElseThrow() : Threshold.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        e.setLevel(dto.getLevel());
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        if (dto.getColorHex() != null) {
            String t = dto.getColorHex().trim();
            e.setColorHex(t.isEmpty() ? null : t);
        }
        return e;
    }

    public PackagingResponseDto toPackagingResponse(Packaging e) {
        if (e == null) {
            return null;
        }
        return PackagingResponseDto.builder().id(e.getId()).code(e.getCode()).description(e.getDescription()).build();
    }

    public Packaging toPackaging(PackagingRequestDto dto) {
        Packaging e = dto.getId() != null ? packagingRepo.findById(dto.getId()).orElseThrow() : Packaging.builder().build();
        if (dto.getCode() != null) {
            e.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            e.setDescription(dto.getDescription());
        }
        return e;
    }

    public ProductResponseDto toProductResponse(Product p) {
        if (p == null) {
            return null;
        }
        Form f = p.getForm();
        DrugType dt = p.getDrugType();
        Category c = p.getCategory();
        Section s = p.getSection();
        Site st = p.getSite();
        Packaging pk = p.getPackaging();
        List<EntityRefDto> thresholdRefs = null;
        if (p.getThresholds() != null && !p.getThresholds().isEmpty()) {
            thresholdRefs = p.getThresholds().stream()
                    .map(th -> ref(th.getId(), th.getCode(), th.getDescription()))
                    .collect(Collectors.toList());
        }
        String photoUrl = (p.getPhotoFilename() != null && !p.getPhotoFilename().isBlank())
                ? ("products/" + p.getId() + "/photo")
                : null;
        return ProductResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .codeBar(p.getCodeBar())
                .famille(p.getFamille())
                .site(st == null ? null : ref(st.getId(), st.getCode(), st.getLibelle()))
                .form(f == null ? null : ref(f.getId(), f.getCode(), f.getDescription()))
                .drugType(dt == null ? null : ref(dt.getId(), dt.getCode(), dt.getDescription()))
                .category(c == null ? null : ref(c.getId(), c.getCode(), c.getDescription()))
                .section(s == null ? null : ref(s.getId(), s.getCode(), s.getDescription()))
                .packaging(pk == null ? null : ref(pk.getId(), pk.getCode(), pk.getDescription()))
                .thresholds(thresholdRefs)
                .dosage(p.getDosage())
                .version(p.getVersion() == null ? 0 : p.getVersion())
                .profilePhotoUrl(photoUrl)
                .build();
    }

    public Product toProduct(ProductRequestDto dto) {
        Product p = dto.getId() != null ? productRepo.findById(dto.getId()).orElseThrow() : Product.builder().version(0).build();
        if (dto.getName() != null) {
            p.setName(dto.getName());
        }
        if (dto.getCodeBar() != null) {
            p.setCodeBar(dto.getCodeBar());
        }
        if (dto.getFamille() != null) {
            String tf = dto.getFamille().trim();
            p.setFamille(tf.isEmpty() ? null : tf);
        }
        if (dto.getSiteId() != null) {
            p.setSite(siteRepository.findById(dto.getSiteId()).orElse(null));
        }
        if (dto.getDosage() != null) {
            p.setDosage(dto.getDosage());
        }
        if (dto.getVersion() != null) {
            p.setVersion(dto.getVersion());
        }
        p.setForm(dto.getFormId() == null ? null : formRepo.findById(dto.getFormId()).orElse(null));
        p.setDrugType(dto.getDrugTypeId() == null ? null : drugTypeRepo.findById(dto.getDrugTypeId()).orElse(null));
        p.setCategory(dto.getCategoryId() == null ? null : categoryRepo.findById(dto.getCategoryId()).orElse(null));
        p.setSection(dto.getSectionId() == null ? null : sectionRepo.findById(dto.getSectionId()).orElse(null));
        p.setPackaging(dto.getPackagingId() == null ? null : packagingRepo.findById(dto.getPackagingId()).orElse(null));
        applyProductThresholds(p, dto.getThresholdIds());
        return p;
    }

    /**
     * Remplace l’association produit ↔ seuils dans {@code product_threshold}.
     * {@code null} = ne pas modifier (mise à jour partielle) ; liste vide = retirer tous les seuils.
     */
    private void applyProductThresholds(Product p, java.util.List<Long> thresholdIds) {
        if (thresholdIds == null) {
            return;
        }
        p.getThresholds().clear();
        for (Long tid : thresholdIds) {
            if (tid != null) {
                thresholdRepo.findById(tid).ifPresent(t -> p.getThresholds().add(t));
            }
        }
    }

    public OrdersResponseDto toOrdersResponse(Orders o) {
        if (o == null) {
            return null;
        }
        Provider pr = o.getProvider();
        AppUser u = o.getUser();
        return OrdersResponseDto.builder()
                .id(o.getId())
                .number(o.getNumber())
                .orderDate(o.getOrderDate())
                .description(o.getDescription())
                .statut(o.getStatut() == null ? null : o.getStatut().name())
                .provider(pr == null ? null : ref(pr.getId(), pr.getCode(), pr.getDesignation()))
                .user(u == null ? null : ref(u.getId(), u.getLogin(), u.getName()))
                .build();
    }

    public Orders toOrders(OrdersRequestDto dto) {
        Orders o = dto.getId() != null ? ordersRepo.findById(dto.getId()).orElseThrow() : Orders.builder().build();
        if (dto.getNumber() != null) {
            o.setNumber(dto.getNumber());
        }
        if (dto.getOrderDate() != null) {
            o.setOrderDate(dto.getOrderDate());
        }
        if (dto.getDescription() != null) {
            o.setDescription(dto.getDescription());
        }
        if (dto.getStatut() != null && !dto.getStatut().isBlank()) {
            o.setStatut(Statut.valueOf(dto.getStatut()));
        }
        if (dto.getProviderId() != null) {
            o.setProvider(providerRepo.findById(dto.getProviderId()).orElse(null));
        }
        if (dto.getUserId() != null) {
            o.setUser(userRepo.findById(dto.getUserId()).orElse(null));
        }
        return o;
    }

    public OrdersDetailsResponseDto toOrdersDetailsResponse(OrdersDetails d) {
        if (d == null) {
            return null;
        }
        Orders ord = d.getOrders();
        Product pr = d.getProduct();
        return OrdersDetailsResponseDto.builder()
                .id(d.getId())
                .orders(ord == null ? null : ref(ord.getId(), ord.getNumber(), ord.getDescription()))
                .product(pr == null ? null : ref(pr.getId(), pr.getCodeBar(), pr.getName()))
                .quantity(d.getQuantity())
                .unitPrice(d.getUnitPrice())
                .discount(d.getDiscount())
                .totalPrice(d.getTotalPrice())
                .build();
    }

    public OrdersDetails toOrdersDetails(OrdersDetailsRequestDto dto) {
        OrdersDetails d = dto.getId() != null ? orderDetailsRepo.findById(dto.getId()).orElseThrow() : OrdersDetails.builder().build();
        if (dto.getOrdersId() != null) {
            d.setOrders(ordersRepo.findById(dto.getOrdersId()).orElse(null));
        }
        if (dto.getProductId() != null) {
            d.setProduct(productRepo.findById(dto.getProductId()).orElse(null));
        }
        d.setQuantity(dto.getQuantity());
        d.setUnitPrice(dto.getUnitPrice());
        d.setDiscount(dto.getDiscount());
        d.setTotalPrice(dto.getTotalPrice());
        return d;
    }

    public ReceiptDetailsResponseDto toReceiptDetailsResponse(ReceiptDetails r) {
        if (r == null) {
            return null;
        }
        AppUser u = r.getUser();
        OrdersDetails od = r.getOrdersDetails();
        return ReceiptDetailsResponseDto.builder()
                .id(r.getId())
                .receivedQuantity(r.getReceivedQuantity())
                .missingQuantity(r.getMissingQuantity())
                .date(r.getDate())
                .observation(r.getObservation())
                .user(u == null ? null : ref(u.getId(), u.getLogin(), u.getName()))
                .ordersDetails(od == null ? null : ref(od.getId(), String.valueOf(od.getId()), "order-line"))
                .build();
    }

    public ReceiptDetails toReceiptDetails(ReceiptDetailsRequestDto dto) {
        ReceiptDetails r = dto.getId() != null ? receiptDetailsRepo.findById(dto.getId()).orElseThrow() : ReceiptDetails.builder().build();
        r.setReceivedQuantity(dto.getReceivedQuantity());
        r.setMissingQuantity(dto.getMissingQuantity());
        if (dto.getDate() != null) {
            r.setDate(dto.getDate());
        }
        if (dto.getObservation() != null) {
            r.setObservation(dto.getObservation());
        }
        if (dto.getUserId() != null) {
            r.setUser(userRepo.findById(dto.getUserId()).orElse(null));
        }
        if (dto.getOrdersDetailsId() != null) {
            r.setOrdersDetails(orderDetailsRepo.findById(dto.getOrdersDetailsId()).orElse(null));
        }
        return r;
    }

    public SellDetailsResponseDto toSellDetailsResponse(SellDetails d) {
        if (d == null) {
            return null;
        }
        Product pr = d.getProduct();
        Batch lot = d.getBatch();
        return SellDetailsResponseDto.builder()
                .id(d.getId())
                .product(pr == null ? null : ref(pr.getId(), pr.getCodeBar(), pr.getName()))
                .batch(lot == null ? null : ref(lot.getId(), lot.getNumber(), lot.getNumber()))
                .quantity(d.getQuantity())
                .discount(d.getDiscount())
                .price(d.getPrice())
                .unitCostAtSale(d.getUnitCostAtSale())
                .build();
    }

    public SellResponseDto toSellResponse(Sell s) {
        if (s == null) {
            return null;
        }
        Site site = s.getSite();
        PointDeVente pdv = s.getPointDeVente();
        AppUser vendeur = s.getEffectueePar();
        List<SellDetailsResponseDto> lines = s.getLignes() == null ? Collections.emptyList()
                : s.getLignes().stream().map(this::toSellDetailsResponse).collect(Collectors.toList());
        return SellResponseDto.builder()
                .id(s.getId())
                .number(s.getNumber())
                .date(s.getDateVente())
                .seller(s.getSeller())
                .client(s.getClient())
                .sellType(s.getSellType())
                .paymentMode(s.getPaymentMode())
                .totalPrice(s.getTotalPrice())
                .amountReceived(s.getAmountReceived())
                .changeGiven(s.getChangeGiven())
                .remark(s.getRemark())
                .site(site == null ? null : ref(site.getId(), site.getCode(), site.getLibelle()))
                .pointDeVente(pdv == null ? null : ref(pdv.getId(), pdv.getCode(), pdv.getLibelle()))
                .effectueePar(
                        vendeur == null ? null : ref(vendeur.getId(), vendeur.getLogin(), vendeur.getName()))
                .lines(lines)
                .build();
    }

    public Sell toSell(SellRequestDto dto) {
        Sell sell = Sell.builder()
                .number(dto.getNumber())
                .dateVente(dto.getDate())
                .seller(dto.getSeller())
                .client(dto.getClient())
                .sellType(dto.getSellType())
                .paymentMode(dto.getPaymentMode())
                .totalPrice(dto.getTotalPrice())
                .amountReceived(dto.getAmountReceived())
                .changeGiven(dto.getChangeGiven())
                .remark(dto.getRemark())
                .lignes(new ArrayList<>())
                .build();
        if (dto.getId() != null) {
            sell.setId(dto.getId());
        }
        if (dto.getSiteId() != null) {
            sell.setSite(siteRepository.findById(dto.getSiteId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            sell.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        if (dto.getEffectueeParUserId() != null) {
            sell.setEffectueePar(userRepo.findById(dto.getEffectueeParUserId()).orElse(null));
        }
        if (dto.getLines() != null) {
            for (SellLineRequestDto line : dto.getLines()) {
                SellDetails sd = SellDetails.builder()
                        .sell(sell)
                        .product(productRepo.findById(line.getProductId()).orElseThrow())
                        .quantity(line.getQuantity())
                        .discount(line.getDiscount())
                        .price(line.getPrice())
                        .batch(line.getBatchId() == null ? null : batchRepo.findById(line.getBatchId()).orElse(null))
                        .unitCostAtSale(line.getUnitCostAtSale())
                        .build();
                if (line.getId() != null) {
                    sd.setId(line.getId());
                }
                sell.getLignes().add(sd);
            }
        }
        return sell;
    }

    public Sell mergeSell(SellRequestDto dto) {
        Sell sell = sellRepo.findById(dto.getId()).orElseThrow();
        if (dto.getNumber() != null) {
            sell.setNumber(dto.getNumber());
        }
        if (dto.getDate() != null) {
            sell.setDateVente(dto.getDate());
        }
        if (dto.getSeller() != null) {
            sell.setSeller(dto.getSeller());
        }
        if (dto.getClient() != null) {
            sell.setClient(dto.getClient());
        }
        if (dto.getSellType() != null) {
            sell.setSellType(dto.getSellType());
        }
        if (dto.getPaymentMode() != null) {
            sell.setPaymentMode(dto.getPaymentMode());
        }
        if (dto.getTotalPrice() != null) {
            sell.setTotalPrice(dto.getTotalPrice());
        }
        if (dto.getAmountReceived() != null) {
            sell.setAmountReceived(dto.getAmountReceived());
        }
        if (dto.getChangeGiven() != null) {
            sell.setChangeGiven(dto.getChangeGiven());
        }
        if (dto.getRemark() != null) {
            sell.setRemark(dto.getRemark());
        }
        if (dto.getSiteId() != null) {
            sell.setSite(siteRepository.findById(dto.getSiteId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            sell.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        if (dto.getEffectueeParUserId() != null) {
            sell.setEffectueePar(userRepo.findById(dto.getEffectueeParUserId()).orElse(null));
        }
        sell.getLignes().clear();
        if (dto.getLines() != null) {
            for (SellLineRequestDto line : dto.getLines()) {
                SellDetails sd = SellDetails.builder()
                        .sell(sell)
                        .product(productRepo.findById(line.getProductId()).orElseThrow())
                        .quantity(line.getQuantity())
                        .discount(line.getDiscount())
                        .price(line.getPrice())
                        .batch(line.getBatchId() == null ? null : batchRepo.findById(line.getBatchId()).orElse(null))
                        .unitCostAtSale(line.getUnitCostAtSale())
                        .build();
                if (line.getId() != null) {
                    sd.setId(line.getId());
                }
                sell.getLignes().add(sd);
            }
        }
        return sell;
    }

    public SellDetails toSellDetails(SellDetailsRequestDto dto) {
        SellDetails d = dto.getId() != null
                ? sellDetailsRepo.findById(dto.getId()).orElseThrow()
                : SellDetails.builder().build();
        if (dto.getSellId() != null) {
            d.setSell(sellRepo.findById(dto.getSellId()).orElse(null));
        }
        if (dto.getProductId() != null) {
            d.setProduct(productRepo.findById(dto.getProductId()).orElse(null));
        }
        d.setQuantity(dto.getQuantity());
        d.setDiscount(dto.getDiscount());
        d.setPrice(dto.getPrice());
        if (dto.getBatchId() != null) {
            d.setBatch(batchRepo.findById(dto.getBatchId()).orElse(null));
        }
        if (dto.getUnitCostAtSale() != null) {
            d.setUnitCostAtSale(dto.getUnitCostAtSale());
        }
        return d;
    }

    public LigneBonCommandeInterneResponseDto toLigneBonResponse(LigneBonCommandeInterne l) {
        if (l == null) {
            return null;
        }
        Product p = l.getProduct();
        Batch bt = l.getBatch();
        return LigneBonCommandeInterneResponseDto.builder()
                .id(l.getId())
                .quantity(l.getQuantity())
                .quantityDelivered(l.getQuantityDelivered())
                .unitPrice(l.getUnitPrice())
                .product(p == null ? null : ref(p.getId(), p.getCodeBar(), p.getName()))
                .batch(bt == null ? null : ref(bt.getId(), bt.getNumber(), bt.getNumber()))
                .build();
    }

    public BonCommandeInterneResponseDto toBonResponse(BonCommandeInterne b) {
        if (b == null) {
            return null;
        }
        Site site = b.getSite();
        PointDeVente pdv = b.getPointDeVente();
        AppUser u = b.getUser();
        MagasinCentral mc = b.getMagasinCentral();
        List<LigneBonCommandeInterneResponseDto> lines = b.getLignes() == null ? Collections.emptyList()
                : b.getLignes().stream().map(this::toLigneBonResponse).collect(Collectors.toList());
        return BonCommandeInterneResponseDto.builder()
                .id(b.getId())
                .number(b.getNumber())
                .orderDate(b.getOrderDate())
                .statut(b.getStatut())
                .statutLibelle(b.getStatut() == null ? null : b.getStatut().getLibelle())
                .commentaire(b.getCommentaire())
                .site(site == null ? null : ref(site.getId(), site.getCode(), site.getLibelle()))
                .pointDeVente(pdv == null ? null : ref(pdv.getId(), pdv.getCode(), pdv.getLibelle()))
                .user(u == null ? null : ref(u.getId(), u.getLogin(), u.getName()))
                .magasinCentral(
                        mc == null ? null : ref(mc.getId(), mc.getCode(), mc.getLibelle()))
                .lines(lines)
                .build();
    }

    private void assignBatchToLigneBon(LigneBonCommandeInterne l, Long batchId) {
        if (batchId == null) {
            l.setBatch(null);
            return;
        }
        l.setBatch(
                batchRepo.findById(batchId).orElseThrow(
                        () -> new IllegalArgumentException("Lot (batch) inconnu id=" + batchId)));
    }

    public BonCommandeInterne toBon(BonCommandeInterneRequestDto dto) {
        BonCommandeInterne b = BonCommandeInterne.builder()
                .number(dto.getNumber())
                .orderDate(dto.getOrderDate())
                .statut(dto.getStatut())
                .commentaire(dto.getCommentaire())
                .lignes(new ArrayList<>())
                .build();
        if (dto.getId() != null) {
            b.setId(dto.getId());
        }
        if (dto.getSiteId() != null) {
            b.setSite(siteRepository.findById(dto.getSiteId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            b.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        if (dto.getUserId() != null) {
            b.setUser(userRepo.findById(dto.getUserId()).orElse(null));
        }
        if (dto.getMagasinCentralId() != null) {
            b.setMagasinCentral(magasinCentralRepository.findById(dto.getMagasinCentralId()).orElse(null));
        }
        if (dto.getLines() != null) {
            for (LigneBonCommandeInterneRequestDto line : dto.getLines()) {
                LigneBonCommandeInterne l = LigneBonCommandeInterne.builder()
                        .bon(b)
                        .product(productRepo.findById(line.getProductId()).orElseThrow())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .build();
                if (line.getId() != null) {
                    l.setId(line.getId());
                }
                assignBatchToLigneBon(l, line.getBatchId());
                b.getLignes().add(l);
            }
        }
        return b;
    }

    public BonCommandeInterneMergeResult mergeBon(BonCommandeInterneRequestDto dto) {
        BonCommandeInterne b = bonCommandeInterneRepository.findById(dto.getId()).orElseThrow();
        StatutBonCommandeInterne statutAvantFusion = b.getStatut();
        if (dto.getNumber() != null) {
            b.setNumber(dto.getNumber());
        }
        if (dto.getOrderDate() != null) {
            b.setOrderDate(dto.getOrderDate());
        }
        if (dto.getStatut() != null) {
            b.setStatut(dto.getStatut());
        }
        if (dto.getCommentaire() != null) {
            b.setCommentaire(dto.getCommentaire());
        }
        if (dto.getSiteId() != null) {
            b.setSite(siteRepository.findById(dto.getSiteId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            b.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        if (dto.getUserId() != null) {
            b.setUser(userRepo.findById(dto.getUserId()).orElse(null));
        }
        if (dto.getMagasinCentralId() != null) {
            b.setMagasinCentral(magasinCentralRepository.findById(dto.getMagasinCentralId()).orElse(null));
        }
        b.getLignes().clear();
        if (dto.getLines() != null) {
            for (LigneBonCommandeInterneRequestDto line : dto.getLines()) {
                LigneBonCommandeInterne l = LigneBonCommandeInterne.builder()
                        .bon(b)
                        .product(productRepo.findById(line.getProductId()).orElseThrow())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .build();
                if (line.getId() != null) {
                    l.setId(line.getId());
                }
                assignBatchToLigneBon(l, line.getBatchId());
                b.getLignes().add(l);
            }
        }
        return new BonCommandeInterneMergeResult(b, statutAvantFusion);
    }

    public AffectationVendeurResponseDto toAffectationResponse(AffectationVendeur a) {
        if (a == null) {
            return null;
        }
        AppUser u = a.getAppUser();
        PointDeVente pdv = a.getPointDeVente();
        return AffectationVendeurResponseDto.builder()
                .id(a.getId())
                .debut(a.getDebut())
                .fin(a.getFin())
                .actifCreneau(a.getActifCreneau())
                .appUser(u == null ? null : ref(u.getId(), u.getLogin(), u.getName()))
                .pointDeVente(
                        pdv == null ? null : ref(pdv.getId(), pdv.getCode(), pdv.getLibelle()))
                .build();
    }

    public AffectationVendeur toAffectation(AffectationVendeurRequestDto dto) {
        AffectationVendeur a = AffectationVendeur.builder()
                .debut(dto.getDebut())
                .fin(dto.getFin())
                .actifCreneau(dto.getActifCreneau() != null ? dto.getActifCreneau() : Boolean.TRUE)
                .build();
        if (dto.getId() != null) {
            a.setId(dto.getId());
        }
        if (dto.getAppUserId() != null) {
            a.setAppUser(userRepo.findById(dto.getAppUserId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            a.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        return a;
    }

    public AffectationVendeur mergeAffectation(AffectationVendeurRequestDto dto) {
        AffectationVendeur a = affectationVendeurRepository.findById(dto.getId()).orElseThrow();
        if (dto.getDebut() != null) {
            a.setDebut(dto.getDebut());
        }
        if (dto.getFin() != null) {
            a.setFin(dto.getFin());
        }
        if (dto.getActifCreneau() != null) {
            a.setActifCreneau(dto.getActifCreneau());
        }
        if (dto.getAppUserId() != null) {
            a.setAppUser(userRepo.findById(dto.getAppUserId()).orElse(null));
        }
        if (dto.getPointDeVenteId() != null) {
            a.setPointDeVente(pointDeVenteRepository.findById(dto.getPointDeVenteId()).orElse(null));
        }
        return a;
    }
}
