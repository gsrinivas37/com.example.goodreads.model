package com.example.goodread.ui;

import model.Book;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class BookTableLabelProvider extends ColumnLabelProvider {
	int col;
	
	public BookTableLabelProvider(int i) {
		this.col = i;
	}

	@Override
	public String getText(Object element) {
		if(element instanceof Book){
			Book book = (Book) element;
			
			switch(col){
			case 0: return book.getName();
			case 1: return book.getAuthor();
			case 2: 
				int rating = book.getAvgRating();
				if(rating==-1)
					return "No Ratings";
				else
					return new Integer(rating).toString();
			}
		}
		
		return "";
	}
}
