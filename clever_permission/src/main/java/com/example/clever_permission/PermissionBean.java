package com.example.clever_permission;

import io.reactivex.Observable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.util.List;

public class PermissionBean {
    public final String name;
    public final boolean granted;
    public final boolean shouldShowRequestPermissionRationale;

    public PermissionBean(String name, boolean granted) {
        this(name, granted, false);
    }

    public PermissionBean(String name, boolean granted, boolean shouldShowRequestPermissionRationale) {
        this.name = name;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    public PermissionBean(List<PermissionBean> permissions) {
        this.name = this.combineName(permissions);
        this.granted = this.combineGranted(permissions);
        this.shouldShowRequestPermissionRationale = this.combineShouldShowRequestPermissionRationale(permissions);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            PermissionBean that = (PermissionBean)o;
            if (this.granted != that.granted) {
                return false;
            } else {
                return this.shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale ? false : this.name.equals(that.name);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + (this.granted ? 1 : 0);
        result = 31 * result + (this.shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    public String toString() {
        return "Permission{name='" + this.name + '\'' + ", granted=" + this.granted + ", shouldShowRequestPermissionRationale=" + this.shouldShowRequestPermissionRationale + '}';
    }

    private String combineName(List<PermissionBean> permissions) {
        return ((StringBuilder) Observable.fromIterable(permissions).map(new Function<PermissionBean, String>() {
            public String apply(PermissionBean permission) throws Exception {
                return permission.name;
            }
        }).collectInto(new StringBuilder(), new BiConsumer<StringBuilder, String>() {
            public void accept(StringBuilder s, String s2) throws Exception {
                if (s.length() == 0) {
                    s.append(s2);
                } else {
                    s.append(", ").append(s2);
                }

            }
        }).blockingGet()).toString();
    }

    private Boolean combineGranted(List<PermissionBean> permissions) {
        return (Boolean)Observable.fromIterable(permissions).all(new Predicate<PermissionBean>() {
            public boolean test(PermissionBean permission) throws Exception {
                return permission.granted;
            }
        }).blockingGet();
    }

    private Boolean combineShouldShowRequestPermissionRationale(List<PermissionBean> permissions) {
        return (Boolean)Observable.fromIterable(permissions).any(new Predicate<PermissionBean>() {
            public boolean test(PermissionBean permission) throws Exception {
                return permission.shouldShowRequestPermissionRationale;
            }
        }).blockingGet();
    }
}
