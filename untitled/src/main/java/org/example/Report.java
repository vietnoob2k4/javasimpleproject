package org.example;

public class Report extends Document{
    private  String field;
    public Report (String author, String title, String docId, String field){
        super(docId, title, author);
        this.field = field;
    }
    public String getField(){
        return field;

    }
    @Override
    public  void displayInfo(){
        super.displayInfo();
        System.out.println("field"+ field);
    }
}
