package com.officine.losto.service;

import com.officine.losto.catalog.MenuSecurityCatalog;
import com.officine.losto.entity.Menu;
import com.officine.losto.model.MenuRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MenuCatalogSyncService {

	private final MenuRepo menuRepo;

	public MenuCatalogSyncService(MenuRepo menuRepo) {
		this.menuRepo = menuRepo;
	}

	/**
	 * Crée ou met à jour les lignes {@code MENU} à partir de {@link MenuSecurityCatalog#fullCatalog()}.
	 *
	 * @return nombre de nœuds enregistrés (insert ou update)
	 */
	@Transactional
	public int syncFromCatalog() {
		int[] counter = {0};
		for (MenuSecurityCatalog.Node root : MenuSecurityCatalog.fullCatalog()) {
			syncRecursive(root, null, counter);
		}
		return counter[0];
	}

	private void syncRecursive(MenuSecurityCatalog.Node def, Menu parent, int[] counter) {
		Menu m = menuRepo.findByPathCode(def.pathCode()).orElseGet(() -> Menu.builder().build());
		m.setPathCode(def.pathCode());
		m.setName(def.name());
		m.setDescription(def.description());
		m.setTreeLevel(def.treeLevel());
		m.setSortOrder(def.sortOrder());
		m.setActive(true);
		m.setParent(parent);
		menuRepo.save(m);
		counter[0]++;
		for (MenuSecurityCatalog.Node child : def.children()) {
			syncRecursive(child, m, counter);
		}
	}
}
