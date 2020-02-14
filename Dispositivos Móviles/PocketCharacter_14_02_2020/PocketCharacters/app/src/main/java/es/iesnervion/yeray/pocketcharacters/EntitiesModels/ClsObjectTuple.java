package es.iesnervion.yeray.pocketcharacters.EntitiesModels;

public class ClsObjectTuple {//Esta clase la utilizamos para un inner join en room

    private int id;
    private String type;
    private String name;
    private String description;
    private String gameMode;
    private int quantity;

    public ClsObjectTuple(){

    }

    //Get Y Set
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
