package com.example.prabhu.btp;
import java.util.Comparator;


public class PDFDoc{
    String name,path;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static Comparator<PDFDoc> StuNameComparator = new Comparator<PDFDoc>() {

        public int compare(PDFDoc s1, PDFDoc s2) {
            String StudentName1 = s1.getName().toUpperCase();
            String StudentName2 = s2.getName().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

}
