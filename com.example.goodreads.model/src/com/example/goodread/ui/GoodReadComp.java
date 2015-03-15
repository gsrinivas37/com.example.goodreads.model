package com.example.goodread.ui;

import model.BookShelf;
import model.DataBase;
import model.Person;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class GoodReadComp extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private DataBase model;
	private ListViewer bookShelveListViewer;
	private Section booksSection;
	private Section sctnProfile;
	private ComboViewer profileComboViewer;
	private Table table;
	private TableViewer tableViewer;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public GoodReadComp(Composite parent, int style) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		
		parent.setLayout(new GridLayout());
		
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(2, false));
		
		Label lblName = toolkit.createLabel(this, "Choose Profile", SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		profileComboViewer = new ComboViewer(this, SWT.READ_ONLY);
		Combo profCombo = profileComboViewer.getCombo();
		profCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(profCombo);
		
		profileComboViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof Person){
					return ((Person)element).getName();
				}
				return "";
			}
		});
		profileComboViewer.setContentProvider(new ArrayContentProvider());
		profCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Person profile = getSelectedProfile();
				profileSelected(profile);
			}
		});
		
		sctnProfile = toolkit.createSection(this, Section.TITLE_BAR);
		sctnProfile.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 2, 1));
		toolkit.paintBordersFor(sctnProfile);
		sctnProfile.setText("Profile");
		
		Composite profileComposite = toolkit.createComposite(sctnProfile, SWT.NONE);
		toolkit.paintBordersFor(profileComposite);
		sctnProfile.setClient(profileComposite);
		profileComposite.setLayout(new GridLayout(3, true));
		
		Section sctnBookShelves = toolkit.createSection(profileComposite, Section.TITLE_BAR);
		sctnBookShelves.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 2, 1));
		toolkit.paintBordersFor(sctnBookShelves);
		sctnBookShelves.setText("Book Shelves");
		
		Composite bookShelvesComposite = toolkit.createComposite(sctnBookShelves, SWT.NONE);
		toolkit.paintBordersFor(bookShelvesComposite);
		sctnBookShelves.setClient(bookShelvesComposite);
		bookShelvesComposite.setLayout(new GridLayout(2, false));
		
		bookShelveListViewer = new ListViewer(bookShelvesComposite, SWT.BORDER | SWT.V_SCROLL);
		List list = bookShelveListViewer.getList();
		list.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		booksSection = toolkit.createSection(bookShelvesComposite, Section.CLIENT_INDENT | Section.TITLE_BAR);
		booksSection.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(booksSection);
		
		Composite booksComp = toolkit.createComposite(booksSection, SWT.NONE);
		toolkit.paintBordersFor(booksComp);
		booksSection.setClient(booksComp);
		booksComp.setLayout(new GridLayout(1, false));
		
		tableViewer = new TableViewer(booksComp, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(table);
		
		TableViewerColumn nameViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnName = nameViewerColumn.getColumn();
		tblclmnName.setWidth(200);
		tblclmnName.setText("Name");
		nameViewerColumn.setLabelProvider(new BookTableLabelProvider(0));
		
		TableViewerColumn authorViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnAuthor = authorViewerColumn.getColumn();
		tblclmnAuthor.setWidth(150);
		tblclmnAuthor.setText("Author");
		authorViewerColumn.setLabelProvider(new BookTableLabelProvider(1));
		
		TableViewerColumn ratingViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnRating = ratingViewerColumn.getColumn();
		tblclmnRating.setWidth(70);
		tblclmnRating.setText("Rating");
		ratingViewerColumn.setLabelProvider(new BookTableLabelProvider(2));
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		
		Section sctnFriends = toolkit.createSection(profileComposite, Section.TITLE_BAR);
		sctnFriends.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
		toolkit.paintBordersFor(sctnFriends);
		sctnFriends.setText("Friends");
		
		bookShelveListViewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof BookShelf){
					return ((BookShelf)element).getName();
				}
				
				return super.getText(element);
			}
		});
		
		bookShelveListViewer.setContentProvider(new ArrayContentProvider());
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BookShelf bookShelf = getSelectedShelve();
				if(bookShelf!=null){
					booksSection.setText(bookShelf.getName());
					populateBooks(booksSection, bookShelf);
				}
			}
		});
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
	}

	protected void profileSelected(Person profile) {
		sctnProfile.setText(profile.getName());
		bookShelveListViewer.setInput(profile.getShelves().toArray());
	}

	protected void populateBooks(Section booksSection, BookShelf bookShelf) {
		tableViewer.setInput(bookShelf.getBooks().toArray());
	}

	public GoodReadComp(Composite container, DataBase model, int none) {
		this(container, none);
		this.model = model;
		
		initialize();
	}

	private BookShelf getSelectedShelve(){
		ISelection selection = bookShelveListViewer.getSelection();
		if(selection instanceof IStructuredSelection){
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			
			if(firstElement instanceof BookShelf){
				return (BookShelf) firstElement;
			}
		}
		return null;
	}
	

	private Person getSelectedProfile(){
		ISelection selection = profileComboViewer.getSelection();
		if(selection instanceof IStructuredSelection){
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			
			if(firstElement instanceof Person){
				return (Person) firstElement;
			}
		}
		return null;
	}
	
	private void initialize() {
		profileComboViewer.setInput(model.getPeople().toArray());
		profileComboViewer.getCombo().select(0);
		
		Person person = getSelectedProfile();
		profileSelected(person);
	}
}
