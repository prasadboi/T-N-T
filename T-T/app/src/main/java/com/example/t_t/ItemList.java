package com.example.t_t;

public class ItemList {
    private String itemTitle;
    private Integer itemQuantity = 0 ;
    private String itemUnits;
    private String itemUrl;
    public ItemList(){ }

    public ItemList(String name, Integer num,String units,String url)
    {
        itemTitle = name;
        itemQuantity = num;
        itemUnits = units;
        itemUrl = url;
    }

    public String getText() { return itemTitle; }
    public void setText(String s) { this.itemTitle = s;}

    public Integer getQuantity() { return itemQuantity; }
    public void setQuantity(Integer in) { this.itemQuantity = in; }
    public String getUnits (){ return itemUnits; }
    public void setUnits (String t) { this.itemUnits = t;}
    public String getItemUrl(){ return itemUrl; }
    public void setItemUrl(String p){ this.itemUrl = p; }
}
