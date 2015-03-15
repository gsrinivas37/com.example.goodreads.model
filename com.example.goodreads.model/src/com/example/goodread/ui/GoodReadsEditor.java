package com.example.goodread.ui;

import java.io.IOException;

import model.DataBase;
import model.ModelPackage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class GoodReadsEditor extends FormEditor implements IResourceChangeListener{

	private DataBase model;
	private IFileEditorInput fileEditorInput;

	public GoodReadsEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	
	@Override
	protected void addPages() {
		createProfilePage();
		createDatabasePage();
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		if(input instanceof IFileEditorInput){
			this.fileEditorInput = (IFileEditorInput) input;
			IFile file = fileEditorInput.getFile();
			
			ModelPackage.eINSTANCE.eClass();
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("model", new XMIResourceFactoryImpl());
			Resource resource = new ResourceSetImpl().getResource(URI.createFileURI(file.getLocation().toPortableString()), true);
			model = (DataBase) resource.getContents().get(0);
		}
	}
	
	private void createProfilePage() {
		Composite profileComp = new GoodReadComp(getContainer(), model, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		profileComp.setLayout(gridLayout);
		
		int index = addPage(profileComp);
		setPageText(index, "Profile");
	}
	
	private void createDatabasePage() {
		Composite profileComp = new DatabasePage(getContainer(), model, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, true);
		profileComp.setLayout(gridLayout);
		
		int index = addPage(profileComp);
		setPageText(index, "Database");
	}
	
	
	private void save(){
		ModelPackage.eINSTANCE.eClass();
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("model", new XMIResourceFactoryImpl());
		Resource resource = new ResourceSetImpl().getResource(URI.createFileURI(fileEditorInput.getFile().getLocation().toPortableString()), true);
		resource.getContents().clear();
		resource.getContents().add(model);
		try {
			resource.save(java.util.Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getType()== IResourceChangeEvent.PRE_CLOSE || event.getType()== IResourceChangeEvent.PRE_DELETE){
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbenchPage[] workbenchPages = getSite().getWorkbenchWindow().getPages();
					
					for(int i=0; i<workbenchPages.length; i++){
						if(fileEditorInput.getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = workbenchPages[i].findEditor(fileEditorInput);
							workbenchPages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}
}
