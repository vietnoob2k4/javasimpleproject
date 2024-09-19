package org.example;

import javax.print.Doc;
import java.util.ArrayList;

public class Manage {
    private ArrayList<Document> documents;
    public  Manage(){
        this.documents = new ArrayList<>();

    }
    public void addDocument(Document document){
        documents.add(document);
    }
    public void displayAllDocument(){
        if (documents.isEmpty()){
            System.out.println("storage empty");
            return;

        }
        for (Document document: documents){
            document.displayInfo();
        }
    }
    public void searchByType(Class<?> type){
        boolean found = false;

        for (Document document: documents){
            if (type.isInstance(document)){
                document.displayInfo();
                found = true;
            }
        }
        if (!found){
            System.out.println("not found");
        }
    }
    public void deleteDocument(String docId){
        Document removeDocId = null;
        for(Document document: documents){
            if (document.getDocId().equals(docId)){
                removeDocId = document;
                break;
            }
        }
        if (removeDocId != null){
            documents.remove(removeDocId);
            System.out.println("Removed");
        }else {
            System.out.println("NOT FOUND");
        }
    }

}
