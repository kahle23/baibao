package baibao.extension.company;

public class CompanyQuery {
    private String id;
    private String name;
    private String type;
    private String code;

    public CompanyQuery(String name) {

        this.name = name;
    }

    public CompanyQuery() {

    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

}
