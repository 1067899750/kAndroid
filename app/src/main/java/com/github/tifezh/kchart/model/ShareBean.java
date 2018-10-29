package com.github.tifezh.kchart.model;

import android.graphics.Bitmap;

/**
 * Description：分享内容
 * Author: star
 * Email: guimingxing@163.com
 * Date: 2018-6-15 16:59
 */

public class ShareBean {

    /**
     * title : this.NewsData.title
     * summary : this.NewsData.summary
     * images : https://pic-test-gjmetal.oss-cn-shanghai.aliyuncs.com/newsv2/c36e6ab2c48047dea929475a71f0f96420180524135234.png
     * href : location.href
     */

    private String title;
    private String summary;
    private String images;
    private String href;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ShareBean(String title, String summary, String images, String href) {
        this.title = title;
        this.summary = summary;
        this.images = images;
        this.href = href;
    }

    public ShareBean(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
