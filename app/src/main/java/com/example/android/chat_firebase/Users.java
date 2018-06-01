package com.example.android.chat_firebase;

/**
 * Created by shivam on 20-03-2018.
 */

public class Users {
    public String device_token;
    public String name;
    public String image;
    public String group;
    public String thumb_image;
    public String email;
    public String uid;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }





    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }




   public Users(){

    }
    public Users(String name, String thumb_image, String Uid){
        this.name = name;
        this.thumb_image = thumb_image;
        this.uid = Uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
