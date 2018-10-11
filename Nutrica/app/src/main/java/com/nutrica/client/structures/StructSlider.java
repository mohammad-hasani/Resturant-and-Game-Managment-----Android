package com.nutrica.client.structures;


public class StructSlider {
    private int id;
    private String pictureUrl;
    private String name;
    private int type;
    private String data;

    public StructSlider(int id, String name, String url)
    {
        setId(id);
        setName(name);
        setUrlPic(url);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrlPic() {
        return pictureUrl;
    }

    public void setUrlPic(String urlPic) {
        this.pictureUrl = urlPic;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
