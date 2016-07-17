package com.softartdev.dailyrssnews;

/**
 * This class handle RSS Item <item> node in rss xml
 * */
public class RSSItem {

    // All <item> node name
    String _title;
    String _link;
    String _description;
    String _pubdate;
    String _guid;
    String _photo;

    // constructor
    public RSSItem(){

    }

    // constructor with parameters
    public RSSItem(String title, String link, String description, String pubdate, String guid, String photo){
        this._title = title;
        this._link = link;
        this._description = description;
        this._pubdate = pubdate;
        this._guid = guid;
        this._photo = photo;
    }

    /**
     * All SET methods
     * */
    public void setTitle(String title){
        this._title = title;
    }

    public void setLink(String link){
        this._link = link;
    }

    public void setDescription(String description){
        this._description = description;
    }

    public void setPubdate(String pubDate){
        this._pubdate = pubDate;
    }

    public void setGuid(String guid){
        this._guid = guid;
    }

    public void setPhoto(String photo) {
        this._photo = photo;
    }

    /**
     * All GET methods
     * */
    public String getTitle(){
        return this._title;
    }

    public String getLink(){
        return this._link;
    }

    public String getDescription(){
        return this._description;
    }

    public String getPubdate(){
        return this._pubdate;
    }

    public String getGuid(){
        return this._guid;
    }

    public String getPhoto() {
        return this._photo;
    }
}
