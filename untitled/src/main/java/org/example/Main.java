package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       Manage manage = new Manage();
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println("1:ADD");
            System.out.println("2.DISPLAY");
            System.out.println("3.SEARCH BY TYPE");
            System.out.println("4.DELETE");
            System.out.println("5.EXIT");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice){
                case 1:
                    System.out.println("Choose document type to add:");
                    System.out.println("1. Book");
                    System.out.println("2. Report");
                    System.out.print("Enter your choice: ");
                    int docTypeChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    System.out.print("Enter Document ID: ");
                    String docId = scanner.nextLine();
                    System.out.print("Enter Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Author: ");
                    String author = scanner.nextLine();

                    if (docTypeChoice == 1) {
                        // Thêm Book
                        System.out.print("Enter Page Count: ");
                        int pageCount = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        manage.addDocument(new Book(docId, title, author, pageCount));
                        System.out.println("Book added successfully!");
                    } else if (docTypeChoice == 2) {
                        // Thêm Report
                        System.out.print("Enter Research Field: ");
                        String researchField = scanner.nextLine();
                        manage.addDocument(new Report(docId, title, author, researchField));
                        System.out.println("Report added successfully!");
                    } else {
                        System.out.println("Invalid document type choice.");
                    }
                    break;
                case 2:
                    manage.displayAllDocument();
                    break;
                case 3:
                    System.out.println("press 1 to find Book press 2 to find Report");
                    int typeChoice = scanner.nextInt();
                    if (typeChoice == 1){
                        manage.searchByType(Book.class);
                    } else if (typeChoice == 2) {
                        manage.searchByType(Report.class);


                    }else {
                        System.out.println("not found");
                    }
                    break;
                case 4:
                    System.out.println("enter id to remove");
                    String docIdToRemove = scanner.nextLine();
                    manage.deleteDocument(docIdToRemove);
                    break;
                case  5:
                    System.out.println("bye");
                    break;
                default:
                    System.out.println("enter from 1 to 5");
            }
        }while (choice != 5);
        scanner.close();
    }
}