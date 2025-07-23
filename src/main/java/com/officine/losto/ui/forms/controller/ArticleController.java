package com.officine.losto.ui.forms.controller;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.Categorie;
import com.officine.losto.backend.entity.Forme;
import com.officine.losto.backend.entity.Lot;
import com.officine.losto.backend.entity.Packaging;
import com.officine.losto.backend.entity.Rayon;
import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.backend.services.ArticleService;
import com.officine.losto.backend.services.CategorieService;
import com.officine.losto.backend.services.FormeService;
import com.officine.losto.backend.services.LotService;
import com.officine.losto.backend.services.PackagingService;
import com.officine.losto.backend.services.RayonService;
import com.officine.losto.backend.services.TyppeService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.ArticleFrame;
import com.officine.losto.ui.forms.FormeFrame;
import com.officine.losto.ui.forms.model.ArticleTableModel;
import com.officine.losto.ui.forms.model.CategorieComboBoxModel;
import com.officine.losto.ui.forms.model.FormeComboBoxModel;
import com.officine.losto.ui.forms.model.LotComboBoxModel;
import com.officine.losto.ui.forms.model.PackagingComboBoxModel;
import com.officine.losto.ui.forms.model.RayonComboBoxModel;
import com.officine.losto.ui.forms.model.TypeComboBoxModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.ArticleValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ArticleController extends AbstractFrameController {

	private final ArticleTableModel tableModel;
	private final ArticleService articleService;
	private final ArticleValidator validator;
	private ArticleFrame frame;
	private final FormeService formeService;
	private final TyppeService typeService;
	private final RayonService rayonService;
	private final LotService lotService;
	private final PackagingService packagingService;
	private final CategorieService categorieService;
	private final LotController lotController;
	private final FormeController formeController;
	private final TyppeController typeController;
	private final CategorieController categorieController;
	private final RayonController rayonController;
	private final PackagingController packagingController;
	

	private void prepareListeners(ArticleFrame frame) {

		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
		registerAction(frame.getBtnSupprimer(), (e) -> remove());
		registerAction(frame.getBtnAddLot(), (e) -> openLotWindow());
		registerAction(frame.getBtnAddForme(), (e) -> openFormeWindow());
		registerAction(frame.getBtnAddType(), (e) -> openTypeWindow());
		registerAction(frame.getBtnAddCagtegorie(), (e) -> openCategorieWindow());
		registerAction(frame.getBtnAddRayon(), (e) -> openRayonWindow());
		registerAction(frame.getBtnAddPackaging(), (e) -> openPackagingWindow());

		/*
		 * Shared.ctrl.formeFrame.addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) {
		 * 
		 * System.out.println("	public void windowClosing(WindowEvent e) { ****"); }
		 * });;
		 */

		frame.getCb_forme().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Forme
				List<String> formeData = formeService.listFormes().stream()
						.map(s -> s.getFormeName().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (formeData.size() == frame.getCb_forme().getItemCount())
					return;
				frame.getCb_forme().setModel(new DefaultComboBoxModel<>(new Vector<>(formeData)));
				


				
			
			}
		});
		
		frame.getCb_lot().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Lot
				List<String> lotData = lotService.listLots().stream()
						.map(s -> s.getNumeroLot().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (lotData.size() == frame.getCb_lot().getItemCount())
					return;
				frame.getCb_lot().setModel(new DefaultComboBoxModel<>(new Vector<>(lotData)));
			
				
			
			}
		});
		
		//Type
		frame.getCb_type().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Type
				List<String> typeData = typeService.listTypes().stream()
						.map(s -> s.getTyppeName().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (typeData.size() == frame.getCb_type().getItemCount())
					return;
				frame.getCb_type().setModel(new DefaultComboBoxModel<>(new Vector<>(typeData)));
			
				
			
			}
		});
		
		
		frame.getCb_categorie().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Categorie
				List<String> catData = categorieService.listCategories().stream()
						.map(s -> s.getCategorieName().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (catData.size() == frame.getCb_categorie().getItemCount())
					return;
				frame.getCb_categorie().setModel(new DefaultComboBoxModel<>(new Vector<>(catData)));
			
				
			
			}
		});
		
		frame.getCb_lot().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Rayon
				List<String> rayonData = rayonService.listRayons().stream()
						.map(s -> s.getRayonCode().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (rayonData.size() == frame.getCb_rayon().getItemCount())
					return;
				frame.getCb_rayon().setModel(new DefaultComboBoxModel<>(new Vector<>(rayonData)));
			
				
			
			}
		});
		
		
		frame.getCb_packaging().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// Packaging
				List<String> condData = packagingService.listPackagings().stream()
						.map(s -> s.getPackagingName().concat(" " + s.getId().toString())).distinct()
						.collect(Collectors.toList());
				if (condData.size() == frame.getCb_packaging().getItemCount())
					return;
				frame.getCb_packaging().setModel(new DefaultComboBoxModel<>(new Vector<>(condData)));
			
				
			
			}
		});
		
		

		frame.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				loadSelectedRow();
			}
		});
		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim());

			}
		});

		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim());

			}
		});
		frame.getTb_codebarre().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {

				frame.getTb_codebarre().selectAll();
			}
		});

		frame.getTb_search().getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkInput();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			public void checkInput() {
				String codeBarre = frame.getTb_codebarre().getText();
				getSelectedItem(codeBarre);

			}

		});

		frame.getTb_codebarre().getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				checkInput();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

			public void checkInput() {
				String codeBarre = frame.getTb_codebarre().getText();
				getSelectedItem(codeBarre);
			}

		});

		// Forme
		List<String> formeData = formeService.listFormes().stream()
				.map(s -> s.getFormeName().concat(" " + s.getId().toString())).distinct().collect(Collectors.toList());
		frame.getEditorforme().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent       e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_forme(), formeData, frame.getEditorforme());
			}
		});

		// Type
		List<String> typeData = typeService.listTypes().stream()
				.map(s -> s.getTyppeName().concat(" " + s.getId().toString())).distinct().collect(Collectors.toList());
		frame.getEditorType().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_type(), typeData, frame.getEditorType());
			}
		});

		// Categorie
		List<String> catData = categorieService.listCategories().stream()
				.map(s -> s.getCategorieName().concat(" " + s.getId().toString())).distinct()
				.collect(Collectors.toList());
		frame.getEditorCategory().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_categorie(), catData, frame.getEditorCategory());
			}
		});

		// Rayon
		List<String> rayonData = rayonService.listRayons().stream()
				.map(s -> s.getRayonName().concat(" " + s.getId().toString())).distinct().collect(Collectors.toList());

		frame.getEditorRayon().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_rayon(), rayonData, frame.getEditorRayon());
			}
		});

		// Conditionnement
		List<String> condData = packagingService.listPackagings().stream()
				.map(s -> s.getPackagingName().concat(" " + s.getId().toString())).distinct()
				.collect(Collectors.toList());
		frame.getEditorConditionnement().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_packaging(), condData,
						frame.getEditorConditionnement());
			}
		});

		frame.getCb_lot().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				loadLot();
			}
		});

		frame.getCb_categorie().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				loadCategorie();
			}
		});

		frame.getCb_packaging().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				loadPackaging();
			}
		});
		frame.getCb_rayon().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				loadRayon();
			}
		});

		frame.getCb_type().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				loadType();
			}
		});

		frame.getCb_forme().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println(" load forme");
				loadForme();
			}
		});

	}

	public void openLotWindow() {
		lotController.prepareAndOpenFrame(frame);
	}

	public void openFormeWindow() {

		formeController.prepareAndOpenFrame(frame);
	}

	public void openTypeWindow() {
		typeController.prepareAndOpenFrame(frame);
	}

	public void openCategorieWindow() {
		categorieController.prepareAndOpenFrame(frame);
	}

	public void openRayonWindow() {
		rayonController.prepareAndOpenFrame(frame);
	}

	public void openPackagingWindow() {
		packagingController.prepareAndOpenFrame(frame);
	}

	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		showFrame();

	}

	private void showFrame() {
		frame = new ArticleFrame(tableModel);
		prepareListeners(frame);
		loadArticles();
		loadForme();
		loadCategorie();
		loadLot();
		loadRayon();
		loadType();
		loadPackaging();
		frame.clearForm();
		Shared.displayFrame(frame);
	}

	private void loadArticles() {
		List<Article> articles = articleService.listArticles();
		tableModel.clear();
		tableModel.addEntities(articles);
	}

	private void loadForme() {
		List<String> formes = formeService.listFormes().stream()
				.map(s -> s.getFormeName().concat(" " + s.getId().toString())).distinct().collect(Collectors.toList());
		frame.getCb_forme().removeAllItems();
		for (String itrem : formes)
			frame.getCb_forme().addItem(itrem);
	}

	private void loadType() {
		List<String> types = typeService.listTypes().stream()
				.map(s -> s.getTyppeName().concat(" ").concat(s.getId().toString())).distinct()
				.collect(Collectors.toList());
		frame.getCb_type().removeAllItems();
		for (String itrem : types)
			frame.getCb_type().addItem(itrem);
	}

	private void loadCategorie() {
		List<String> categories = categorieService.listCategories().stream()
				.map(s -> s.getCategorieName().concat(" ").concat(s.getId().toString())).distinct()
				.collect(Collectors.toList());

		frame.getCb_categorie().removeAllItems();
		for (String itrem : categories)
			frame.getCb_categorie().addItem(itrem);

	}

	private void loadRayon() {
		List<String> rayons = rayonService.listRayons().stream()
				.map(s -> s.getRayonName().concat(" ").concat(s.getId().toString())).distinct()
				.collect(Collectors.toList());
		rayons.add(Strings.EMPTY);

		frame.getCb_rayon().removeAllItems();
		for (String itrem : rayons)
			frame.getCb_rayon().addItem(itrem);
	}

	private void loadLot() {
		List<String> lots = lotService.listLots().stream()
				.map(s -> s.getNumeroLot().concat(" ").concat(s.getId().toString())).distinct()
				.collect(Collectors.toList());

		frame.getCb_lot().removeAllItems();
		for (String itrem : lots)
			frame.getCb_lot().addItem(itrem);

	}

	private void loadPackaging() {
		List<String> packagings = packagingService.listPackagings().stream()
				.map(s -> s.getPackagingName().concat(" ").concat(s.getId().toString())).distinct()
				.collect(Collectors.toList());

		frame.getCb_packaging().removeAllItems();
		for (String itrem : packagings)
			frame.getCb_packaging().addItem(itrem);
	}

	private void findEntitybyCriteria(String codeBarre, String articlename) {
		List<Article> articles = articleService.findArticleByCriteria(codeBarre, articlename);
		tableModel.clear();
		tableModel.addEntities(articles);
	}

	private void loadSelectedRow() {

		if (tableModel.getRowCount() == 0)
			return;
		int i = frame.getTable().getSelectedRow();

		if (frame.getTable().getValueAt(i, 2) != null) {
			frame.getTb_codebarre().setText(frame.getTable().getValueAt(i, 2).toString());
			getSelectedItem(frame.getTb_codebarre().getText());

			frame.getTable().getSelectionModel().addSelectionInterval(i, i);
		}

	}

	private void getSelectedItem(String codebarre) {

		Article articleToExtract = articleService.loadByArticleCodeBarre(codebarre);

		if (articleToExtract == null) {

			frame.clearForm();
			return;
		}
		// ID

		if (articleToExtract.getId() != null)
			frame.getTb_id().setText(articleToExtract.getId().toString());

		if (articleToExtract.getArticleName() != null)
			frame.getTb_nomarticle().setText(articleToExtract.getArticleName().toString());

		if (articleToExtract.getArticleDescription() != null)
			frame.getTb_description().setText(articleToExtract.getArticleDescription().toString());

		if (articleToExtract.getArticleForme() != null)
			frame.getCb_forme().setSelectedItem(articleToExtract.getArticleForme().getFormeName()
					.concat(" " + articleToExtract.getArticleForme().getId()).toString());

		if (articleToExtract.getArticleTyppe() != null)
			frame.getCb_type().setSelectedItem(articleToExtract.getArticleTyppe().getTyppeName()
					.concat(" " + articleToExtract.getArticleTyppe().getId()).toString());

		if (articleToExtract.getArticleCategorie() != null)
			frame.getCb_categorie().setSelectedItem(articleToExtract.getArticleCategorie().getCategorieName()
					.concat(" " + articleToExtract.getArticleCategorie().getId()).toString());

		if (articleToExtract.getArticleRayon() != null)
			frame.getCb_rayon().setSelectedItem(articleToExtract.getArticleRayon().getRayonName()
					.concat(" " + articleToExtract.getArticleRayon().getId()).toString());

		if (articleToExtract.getArticleDosage() != null)
			frame.getTb_dosage().setText(articleToExtract.getArticleDosage());

		if (articleToExtract.getArticlePackaging() != null)
			frame.getCb_packaging().setSelectedItem(articleToExtract.getArticlePackaging().getPackagingName()
					.concat(" " + articleToExtract.getArticlePackaging().getId()).toString());

		if (articleToExtract.getArticleLot() != null)
			frame.getCb_lot().setSelectedItem(articleToExtract.getArticleLot().getNumeroLot()
					.concat(" " + articleToExtract.getArticleLot().getId()).toString());
		frame.getTb_quantite().setText("" + articleToExtract.getArticleQuantite_stock());

		frame.getTb_prix_achat().setText("" + articleToExtract.getArticlePrixAchat());

		frame.getTb_prix_vente().setText("" + articleToExtract.getArticlePrixVente());

	}

	private void save() {
		Article article = frame.getArticleFromForm();

		// type
		String[] typesIds = frame.getCb_type().getSelectedItem().toString().split(" ");
		Typpe typeEntity = typeService.loadTypeById(Long.parseLong(typesIds[typesIds.length - 1]));
		article.setArticleTyppe(typeEntity);

		// Forms
		String[] formes = frame.getCb_forme().getSelectedItem().toString().split(" ");
		Forme forme = formeService.loadFormeById(Long.parseLong(formes[formes.length - 1]));
		article.setArticleForme(forme);

		// category
		String[] CategoryIds = frame.getCb_categorie().getSelectedItem().toString().split(" ");
		Categorie category = categorieService.loadCategorieById(Long.parseLong(CategoryIds[CategoryIds.length - 1]));
		article.setArticleCategorie(category);

		// lot
		if (frame.getCb_lot().getSelectedItem() != null) {
			String[] lotIds = frame.getCb_lot().getSelectedItem().toString().split(" ");
			Lot lot = lotService.loadLotById(Long.parseLong(lotIds[lotIds.length - 1]));
			article.setArticleLot(lot);
		}

		// rayon

		if (frame.getCb_rayon().getSelectedItem().toString().equalsIgnoreCase(Strings.EMPTY))
			article.setArticleRayon(null);
		else {

			String[] rayonIds = frame.getCb_rayon().getSelectedItem().toString().split(" ");
			Rayon rayon = rayonService.loadRayonById(Long.parseLong(rayonIds[rayonIds.length - 1]));
			article.setArticleRayon(rayon);
		}

		// Packaging
		String[] packagingIds = frame.getCb_packaging().getSelectedItem().toString().split(" ");
		Packaging packaging = packagingService.loadPackagingById(Long.parseLong(packagingIds[packagingIds.length - 1]));
		article.setArticlePackaging(packaging);

		Optional<ValidationError> errors = validator.validate(article);

		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}
		if (frame.getTb_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			article = articleService.save(article);

			tableModel.addEntity(article);
			frame.clearForm();
			return;
		}
		articleService.saveAndFlush(article);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, article);
		frame.clearForm();
	}

	private void closeModalWindow() {
		frame.clearForm();
		frame.dispose();
	}

	private void remove() {
		try {
			JTable table = frame.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0) {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				confirmDialog.showInfo(frame, ConstMessagesEN.Messages.NON_ROW_SELECTED);
				return;

			} else {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					Article Article = tableModel.getEntityByRow(selectedRow);
					articleService.remove(Article);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
