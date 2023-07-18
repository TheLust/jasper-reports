package org.example;

public class Holiday {
    private String country;
    private String name;
    private String date;

    public Holiday(String country, String name, String date) {
        this.country = country;
        this.name = name;
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    @Override
//    public String toString() {
//        return "Holiday{" +
//                "country='" + country + '\'' +
//                ", name='" + name + '\'' +
//                ", date=" + date +
//                '}';
//    }
}
