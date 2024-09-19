package org.example;

public class Book extends Document{
    private int pages;
    public Book(String title, String docId, String author,int pages){
        super(docId, title, author);
        this.pages = pages;

    }
    public int getPages(){
        return pages;
    }
    @Override
    public void displayInfo(){
        super.displayInfo();
        System.out.println("Pages:" + pages);
    }
}
