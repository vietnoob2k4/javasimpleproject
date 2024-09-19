package org.example;

public class Document {
    private String docId;

    private String title;
    private String author;

    public Document(String docId, String title, String author){
        this.docId = docId;
        this.author = author;
        this.title = title;


    }
    public String getDocId(){
        return docId;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public void displayInfo(){
        System.out.println("id: "+ docId + "title" + title + "author" + author);
    }
}
