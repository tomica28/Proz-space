package model;

public enum SHIP {
    BLUE("view/resources/shipchooser/blue_ship.png", "view/resources/shipchooser/blueLife.png"),
    GREEN("view/resources/shipchooser/green_ship.png", "view/resources/shipchooser/greenLife.png"),
    ORANGE("view/resources/shipchooser/orange_ship.png", "view/resources/shipchooser/orangeLife.png"),
    RED("view/resources/shipchooser/red_ship.png", "view/resources/shipchooser/redLife.png");

    private String urlShip;
    private String urlLife;

    SHIP(String urlShip, String urlLife){
        this.urlShip = urlShip;
        this.urlLife = urlLife;
    }

    public String getUrl() {
        return this.urlShip;
    }

    public String getUrlLife() { return this.urlLife; }
}
