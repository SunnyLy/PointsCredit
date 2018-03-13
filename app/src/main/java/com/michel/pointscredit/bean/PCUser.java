package com.michel.pointscredit.bean;

import android.support.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by 80010651 on 2018/3/13.
 * 用户信息实体类：
 * 继承自ParseUser后，拥有了基本属性，
 * 然后可以自定义扩展需要属性
 */

@ParseClassName("PCUser")
public class PCUser extends ParseUser {

    private static final String CONTACT_EMAIL = "contactEmail";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String PHONE = "phone";
    private static final String IS_ADMIN = "isAdmin";

    public String getContactEmail() {
        return getString(CONTACT_EMAIL);
    }

    public void setContactEmail(@NonNull String contactEmail) {
        put(CONTACT_EMAIL, contactEmail);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public void setFirstName(@NonNull String firstName) {
        put(FIRST_NAME, firstName);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public void setLastName(@NonNull String lastName) {
        put(LAST_NAME, lastName);
    }

    public String getPhone() {
        return getString(PHONE);
    }

    public void setPhone(@NonNull String phone) {
        put(PHONE, phone);
    }

    public boolean isAdmin() {
        return getBoolean(IS_ADMIN);
    }

    public void setAdmin(boolean admin) {
        put(IS_ADMIN, admin);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
