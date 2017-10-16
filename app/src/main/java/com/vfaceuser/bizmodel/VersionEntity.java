package com.vfaceuser.bizmodel;

/**
 * 版本更新
 *
 * @author Fangweidong
 */
public class VersionEntity {

    private String version_name;
    private int version_code;
    private int is_force;
    private String url;
    private String release_note;

    public int getIs_force() {
        return is_force;
    }

    public void setIs_force(int is_force) {
        this.is_force = is_force;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getRelease_note() {
        return release_note;
    }

    public void setRelease_note(String release_note) {
        this.release_note = release_note;
    }

}
