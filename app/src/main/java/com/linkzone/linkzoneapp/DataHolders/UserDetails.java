package com.linkzone.linkzoneapp.DataHolders;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by Tiwaris on 3/14/2017.
 */

public class UserDetails {

    public static Activity getHome() {
        return Home;
    }

    public static void setHome(Activity home) {
        Home = home;
    }

    public static Activity Home;
//    public static ArrayList<SearchList> autoCompleteItems;
//
//    public static ArrayList<SearchList> getAutoCompleteItems() {
//        return autoCompleteItems;
//    }
//
//    public static void setAutoCompleteItems(ArrayList<SearchList> autoCompleteItems) {
//        UserDetails.autoCompleteItems = autoCompleteItems;
//    }


    public static int logintype=0;

    public static String UID;
    public static String UName;
    public static String UEmail;
    public static String UMob;
    public static String UImage;
    public static String UDob;
    public static String Ustatus;
    public static String Uinterestin;
    public static String Ulives;
    public static String Uworkin;
    public static String Uworkat;
    public static String UHHC;
    public static String USSC;
    public static String UGradu;
    public static String user_gender;
    public static String cover_pic;
    public static String socialImage;

    public static String getSocialImage() {
        return socialImage;
    }

    public static void setSocialImage(String socialImage) {
        UserDetails.socialImage = socialImage;
    }

    public static String getCover_pic() {
        return cover_pic;
    }

    public static void setCover_pic(String cover_pic) {
        UserDetails.cover_pic = cover_pic;
    }

    public static String getUser_gender() {
        return user_gender;
    }

    public static void setUser_gender(String user_gender) {
        UserDetails.user_gender = user_gender;
    }

    public static String getProfileP() {
        return profileP;
    }

    public static void setProfileP(String profileP) {
        UserDetails.profileP = profileP;
    }

    public static String UPostGra;
    public static String Uadd_website;
    public static String chat_id;
    public static String profileP;

    public static String getChat_id() {
        return chat_id;
    }

    public static void setChat_id(String chat_id) {
        UserDetails.chat_id = chat_id;
    }

    public static String getReset_code() {
        return reset_code;
    }

    public static void setReset_code(String reset_code) {
        UserDetails.reset_code = reset_code;
    }

    public static String reset_code;

    public static String getUDob() {
        return UDob;
    }

    public static void setUDob(String UDob) {
        UserDetails.UDob = UDob;
    }

    public static String getUstatus() {
        return Ustatus;
    }

    public static void setUstatus(String ustatus) {
        Ustatus = ustatus;
    }

    public static String getUinterestin() {
        return Uinterestin;
    }

    public static void setUinterestin(String uinterestin) {
        Uinterestin = uinterestin;
    }

    public static String getUlives() {
        return Ulives;
    }

    public static void setUlives(String ulives) {
        Ulives = ulives;
    }

    public static String getUworkin() {
        return Uworkin;
    }

    public static void setUworkin(String uworkin) {
        Uworkin = uworkin;
    }

    public static String getUworkat() {
        return Uworkat;
    }

    public static void setUworkat(String uworkat) {
        Uworkat = uworkat;
    }

    public static String getUHHC() {
        return UHHC;
    }

    public static void setUHHC(String UHHC) {
        UserDetails.UHHC = UHHC;
    }

    public static String getUSSC() {
        return USSC;
    }

    public static void setUSSC(String USSC) {
        UserDetails.USSC = USSC;
    }

    public static String getUGradu() {
        return UGradu;
    }

    public static void setUGradu(String UGradu) {
        UserDetails.UGradu = UGradu;
    }

    public static String getUPostGra() {
        return UPostGra;
    }

    public static void setUPostGra(String UPostGra) {
        UserDetails.UPostGra = UPostGra;
    }

    public static String getUadd_website() {
        return Uadd_website;
    }

    public static void setUadd_website(String uadd_website) {
        Uadd_website = uadd_website;
    }

    public static String getUfacebook_page() {
        return Ufacebook_page;
    }

    public static void setUfacebook_page(String ufacebook_page) {
        Ufacebook_page = ufacebook_page;
    }

    public static String Ufacebook_page;

    public static String getUID() {
        return UID;
    }

    public static String getUImage() {
        return UImage;
    }

    public static int getLogintype() {
        return logintype;
    }

    public static void setLogintype(int logintype) {
        UserDetails.logintype = logintype;
    }

    public static void setUImage(String UImage) {
        UserDetails.UImage = UImage;
    }

    public static void setUID(String UID) {
        UserDetails.UID = UID;
    }

    public static String getUName() {
        return UName;
    }

    public static void setUName(String UName) {
        UserDetails.UName = UName;
    }

    public static String getUEmail() {
        return UEmail;
    }

    public static void setUEmail(String UEmail) {
        UserDetails.UEmail = UEmail;
    }

    public static String getUMob() {
        return UMob;
    }

    public static void setUMob(String UMob) {
        UserDetails.UMob = UMob;
    }
}
