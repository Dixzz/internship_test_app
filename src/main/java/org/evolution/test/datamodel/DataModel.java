package org.evolution.test.datamodel;

public class DataModel {
    private String name;
    private String capital;
    private String flag;
    private String region;
    private String subregion;
    private String pop;
    private String bord;
    private String lang;
    private String code;

    public DataModel(String code, String name, String capital, String flag, String region, String subregion, String pop, String bord, String lang) {
        this.code = code;
        this.name = name;
        this.capital = capital;
        this.flag = flag;
        this.region = region;
        this.subregion = subregion;
        this.pop = pop;
        this.bord = bord;
        this.lang = lang;
    }

    private static String coolFormat(double n, int iteration) {
        char[] c = new char[]{'K', 'M', 'B', 'T'};
        double d = Math.ceil((n / 100) / 10.0);
        boolean isRound = (d * 10) % 10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        return (d < 1000 ? //this determines the class, i.e. 'k', 'm' etc
                ((d > 99.9 || isRound || (!isRound && d > 9.99) ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + c[iteration])
                : coolFormat(d, iteration + 1));
    }

    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public String getFlag() {
        return flag;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public String getPop() {
        return "Population: " + coolFormat(Float.parseFloat(pop), 0);
    }

    public String getBord() {
        return bord;
    }

    public String getLang() {
        if (lang.contains("("))
            return lang.substring(0, lang.indexOf("("));
        else
            return lang;
    }

    public String getCode() {
        return code;
    }
}
