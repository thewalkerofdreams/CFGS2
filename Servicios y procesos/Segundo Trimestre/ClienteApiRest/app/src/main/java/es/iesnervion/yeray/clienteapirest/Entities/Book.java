package es.iesnervion.yeray.clienteapirest.Entities;

public class Book {
    private int code;
    private String name;
    private int numPages;

    public Book(){
        code = 0;
        name = "DEFAULT";
        numPages = 0;
    }

    public Book(int code, String name, int numPages){
        this.code = code;
        this.name = name;
        this.numPages = numPages;
    }

    //Get Y Set
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
}
