package com.example.goodread.ui;

import model.DataBase;
import model.Person;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;

public class DatabasePage extends Composite {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private Table table;
	private DataBase model;
	private TableViewer tableViewer;
	private ListViewer listViewer;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public DatabasePage(Composite parent, int style) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(3, true));
		
		Section sctnPeople = toolkit.createSection(this, Section.TITLE_BAR);
		sctnPeople.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(sctnPeople);
		sctnPeople.setText("People");
		
		listViewer = new ListViewer(sctnPeople, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		sctnPeople.setClient(list);
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new LabelProvider(){
			public String getText(Object element) {
				if(element instanceof Person){
					return ((Person)element).getName();
				}
				return "";
			};
		});
		
		Section sctnBooks = toolkit.createSection(this, Section.TITLE_BAR);
		sctnBooks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		sctnBooks.setBounds(0, 0, 99, 20);
		toolkit.paintBordersFor(sctnBooks);
		sctnBooks.setText("Books");
		
		tableViewer = new TableViewer(sctnBooks, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		toolkit.paintBordersFor(table);
		sctnBooks.setClient(table);
		
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

	}

	public DatabasePage(Composite container, DataBase model, int none) {
		this(container,none);
		this.model = model;
		
		initialize();
	}

	private void initialize() {
		tableViewer.setInput(model.getBooks().toArray());
		listViewer.setInput(model.getPeople().toArray());
	}
}
